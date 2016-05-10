(ns clj-counterpoint.core
  [:use
   [overtone.live]
   [overtone.inst.piano]
   [mud.core]]
  [:require
   [clj-counterpoint.gen :as gen]
   [mud.timing :as time]])

(:beat time/main-beat)
(:count time/main-beat)
(ctl time/root-s :rate 8.)

(def dasfunk '(:G4  :G4  :G4  :G4  :G4  :G4  :G4  :G4
               :F4  :F4  :G4  :G4  :Bb4 :Bb4 :D4  :D4
               :D4  :D4  :D4  :D4  :D4  :D4  :D4  :D4
               :C4  :C4  :D4  :D4  :F4  :F4  :Bb3 :Bb3

               :Bb3 :Bb3 :Bb3 :Bb3 :Bb3 :Bb3 :Bb3 :Bb3
               :A3  :A3  :Bb3 :Bb3 :D4  :D4  :G3  :G3
               :G3  :G3  :G3  :G3  :G3  :G3  :A3  :A3
               :A3  :A3  :Bb3 :Bb3 :Bb3 :Bb3 :G4  :G4 ))
(def dasharmony (vec (for [x dasfunk] (note x))))

(defasynth player [notes [30 30] amp 1 beat-bus time/beat-b out-bus 0]
  (let [cnt (in:kr beat-bus)
        note (buf-rd:kr 1 notes cnt)
        freq (midicps note)
        src (sin-osc freq)]
    (out out-bus (pan2 (* amp src)))))

(def daft (buffer 64))
(def dafth (buffer 64))
(pattern! daft dasfunk)
(pattern! dafth (gen/generate-harmony dasharmony))

(def melody (player :notes daft :beat-bus (:count time/main-beat)))
#_(kill melody)
(def harmony (player :notes dafth :beat-bus (:count time/main-beat) :amp 1))
#_(kill harmony)
#_(stop)
