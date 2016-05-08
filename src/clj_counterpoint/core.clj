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


(def daft (buffer 64))
(mud/pattern! daft dasfunk)

(defsynth foo [freq 440 amp 1 rate 500 rateBus time/root-b out-bus 0]
  (let [sine    (sin-osc:ar freq)
        slosine (sin-osc:kr (in:kr rateBus))
        saw     (saw rate)
        amp     (* amp slosine)
        sound   (* sine saw amp)]
    (out out-bus (pan2 sound)))
  )

#_(mud/defasynth sin-ly [notes [30 30 30 40 30] amp 1 beat-bus time/beat-b out-bus 0]
  (let [cnt (in:kr beat-bus)
        note (buf-rd:kr 1 notes cnt)
        freq (midicps note)
        src (saw freq)]
    (out out-bus (pan2 (* amp src)))))

#_(def s-l (sin-ly :notes daft :beat-bus (:count time/main-beat)))
(kill s-l)
