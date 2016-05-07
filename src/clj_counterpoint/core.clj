(ns clj-counterpoint.core
  [:use
   [overtone.live]
   [overtone.inst.piano]]
  [:require
   [clj-counterpoint.gen :as gen]
   [mud.core :as mud]
   [mud.timing :as time]])
