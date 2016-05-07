(ns clj-counterpoint.gen)

(declare get-hist get-contour new-contour new-cu-step new-step apply-step)

(defn generate-harmony [melody harmony]
  (doseq [m melody]
    (let [hist (get-hist harmony)]
      (->>
       hist
       (get-contour)
       (new-contour)
       (new-cu-step)
       (new-step m)
       (apply-step harmony))
      harmony)))

#_(def harmony (generate-harmony [67 67 45 56 67 78 77 66 65 66] []))

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
  []
  ())

(defn new-cu-step
  ""
  []
  ())

(defn new-step
  ""
  []
  ())

(defn apply-step
  ""
  []
  ())

(defn -main []
  ())
