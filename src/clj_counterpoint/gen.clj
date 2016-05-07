(ns clj-counterpoint.gen)

(declare generator get-hist get-contour new-contour new-cu-step new-step apply-step)

(defn generate-harmony
  "takes a melody and returns a harmony"
  [melody]
  (let [cnt (count melody)]
    (take cnt (generator melody))))

#_(def h (generate-harmony (seq '(67 67 45 56 67 78 77 66 65 66))))
#_(println (last h))

(defn- generator
  "I need to know:
    history of the harmony
    current melody note *for each harmony item
    harmony vector"
  ([melody] ; when given just the melody, takes the starting note as the starting harmony note
   (generator melody (cons (first melody) ())))
  ([melody harmony] ; where the real fun happens
   (let [hist (get-hist harmony 3)]
     (cons harmony
           (lazy-seq
            (->>
             hist
             (get-contour)
             (new-contour (- (count hist) 1))
             (new-cu-step)
             (new-step (peek melody))
             (apply-step harmony)
             (generator melody)))))))

#_(println (take 10 (generator (seq '(1 2 3 4 5 6 7 8 9 10)))))

(defn- get-hist
  "returns the most recent (up to) [num] notes in [harmony]"
  [harmony num]
  (take 3 harmony))

(defn- get-contour
  "determines whether [hist] contains a melodic contour, and if so, what it is.
  (^^)=2   (^-)= 1   (^v) = 0   (v-) = -1 (vv) = -2"
  [hist]
  (let [len (count hist)]
    (loop [i 1 hist hist sum 0] ; i = 1 because we don't care about just the first value; only pairs
      (if (< i len)
        (let [now  (first hist)
              ignoreme (prn (type now))
              next (first (rest hist))
              ignoreme (prn (type next))]
          (->>
           (cond
             (< now next)  1
             (> now next) -1
             :else         0)
           (+ sum)
           (recur (inc i) (rest hist))))))))

(defn- new-contour
  "determines which direction the new step will go in.
  the number passed affects the probability of a given direction.
  direction is returned as 1, -1, or 0"
  [prob-size prev-direction]
  -1)

(defn- new-cu-step
  "chooses a step size, in 'consonant units'.
  probabilities are weighted inside the cond"
  [direction]
  (let [n (rand 25)]
    (->>
     (cond
       (< n  5) 0
       (< n 15) 1
       (< n 21) 2
       (< n 24) 3
       (< n 25) 4
       :else    nil)
     (* direction))))

(defn- new-step
  "takes step size in cu and translates to midi steps, based on consonances with the current melody note"
  [m-note step]
  (let [sign (Math/pow step 0)]
    (->>
     (Math/abs step)
     (nth [0 2 4 5 7])
     (* sign) ; multiplies step size by positive or negative one
     (+ m-note))))
;;in western counterpoint, consonant intervals are:
;; 3rd  5th  6th  8th
;; 2    4    5    7

(defn- apply-step
  "applies step to most recent harmony note"
  [harmony step]
  (conj harmony step))

(defn -main []
  ())
