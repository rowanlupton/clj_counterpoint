(ns clj-counterpoint.gen
  (:require [clojure.math.numeric-tower :as math]))

(declare generator get-hist get-contour new-contour new-cu-step new-step apply-step get-sign)

(defn generate-harmony
  "takes a melody and returns a harmony"
  [melody]
  (let [cnt (count melody)]
    (reverse (take cnt (generator melody)))))

#_(println (generate-harmony (seq '(67 67 45 56 67 78 77 66 65 66))))
#_(println h)

(defn- generator
  "calls gen functions in turn.
  returns "
  ([melody] ; when given just the melody, takes the starting note as the starting harmony note
   (generator (pop melody) (conj '() (first melody))))
  ([melody harmony] ; where the real fun happens
   (let [hist (get-hist harmony 3)]
     (cons harmony
           (lazy-seq
            (->>
             hist
             (get-contour)
             (new-contour)
             (new-cu-step)
             (new-step (peek melody))
             (apply-step harmony)
             (generator melody)))))))

#_(println (take 10 (generator (seq '(1 2 3 4 5 6 7 8 9 10)))))

(defn- get-hist
  "returns the most recent (up to) [num] notes in [harmony]"
  [harmony num]
  #_(prn "GET-HIST       harmony" harmony)
  #_(prn "GET-HIST       num" num)
  (take 3 harmony))

(defn- get-contour
  "determines whether [hist] contains a melodic contour, and if so, what it is.
  (^^)=2   (^-)= 1   (^v) = 0   (v-) = -1 (vv) = -2"
  [hist]
  #_(prn "GET-CONTOUR    hist" hist)
  (let [len (count hist)]
    (loop [i 1 hist hist sum 0] ; i = 1 because we don't care about just the first value; only pairs
      (if (< i len)
        (let [now  (first hist)
              next (first (rest hist))]
          (->>
           (cond
             (< now next)  1
             (> now next) -1
             :else         0)
           (+ sum)
           (recur (inc i) (rest hist))))sum))))

(defn- get-sign
  "tests for negative, zero, or positive
  returns: -1, 0, or 1"
  [n]
  #_(prn "GET-SIGN       n" n)
  (cond
    (> n  0)  1
    (< n -0) -1
    :else    0))

(defn- new-contour
  "determines which direction the new step will go in.
  the number passed affects the probability of a given direction.
  direction is returned as 1, 0, or -1"
  [prev-contour]
  (let [s (get-sign prev-contour)]
    #_(prn "NEW-CONTOUR    prev-contour" prev-contour)
    (->>
     (case s
       0 (+ s 3)
       (* s 3))
     (+ prev-contour)
     (rand-int)
     (- s)
     (get-sign))))
(new-contour 1)

(defn- new-cu-step
  "chooses a step size, in 'consonant units'.
  probabilities are weighted inside the cond
  returns a number between + and - 4"
  [direction]
  #_(prn "NEW-CU-STEP    direction" direction)
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
  "takes step size in cu and translates to midi steps, based on consonances with the current melody note
  returns: "
  [m-note step]
  #_(prn "NEW-STEP       m-note" m-note)
  #_(prn "NEW-STEP       step" step)
  (let [sign (get-sign step)]
    (->>
     (math/abs step)
     (nth [0 2 4 5 7])
     (* sign) ; multiplies step size by positive or negative one
     )))
;;in western counterpoint, consonant intervals are:
;; 3rd  5th  6th  8th
;; 2    4    5    7
(new-step 56 2)

(defn- apply-step
  "applies step to most recent harmony note"
  [harmony step]
  #_(prn "APPLY-STEP     apply-step harmony" harmony)
  #_(prn "APPLY-STEP     peek the above" (peek harmony))
  #_(prn "APPLY-STEP     apply-step step" step)
  #_(prn "APPLY-STEP     + some stuff" (+ (peek harmony) step))
  #_(prn "--------------------------------------------")
  (conj harmony (+ (peek harmony) step)))
#_(prn (apply-step [44 46 47 43 44] -3))

(defn -main []
  ())
