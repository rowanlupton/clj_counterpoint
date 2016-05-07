(ns clj-counterpoint.gen)

(declare generator get-hist get-contour new-contour new-cu-step new-step apply-step)

(defn generate-harmony
  "takes a melody and returns a harmony"
  [melody]
  (let [cnt (count melody)]
    (take cnt (generator melody))))

#_(generate-harmony [67 67 45 56 67 78 77 66 65 66])

(defn- generator
  "I need to know:
    history of the harmony
    current melody note *for each harmony item
    harmony vector"
  ([melody]
   (generator melody (first melody)))
  ([melody harmony]
   #_(println "made it this far")
   (let [hist (get-hist harmony)]
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

#_(println (take 4 (generator [75 79 78 70 71 73])))

(defn get-hist
  ""
  [harmony]
  ())

(defn get-contour
  ""
  [hist]
  ())

(defn new-contour
  ""
  [foo]
  ())

(defn new-cu-step
  ""
  [foo]
  ())

(defn new-step
  ""
  [foo bar]
  ())

(defn apply-step
  ""
  [foo bar]
  )

(defn -main []
  ())
