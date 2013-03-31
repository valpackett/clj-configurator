(ns clj-configurator.util
  (:use coercer.core)
  (:require [clojure.string :as string]))

(defmethod coerce [String Boolean] [s _]
  (boolean (#{true "true" "True" "TRUE" 1 "1" "yes" "on"} s)))

(defn to-type-of [x y]
  (if (nil? x)
    y
    (coerce y (class x))))

(defn string-variants [x]
  [x
   (-> x string/upper-case)
   (-> x (string/replace "-" "_"))
   (-> x (string/replace "-" "_") string/upper-case)])

(defmacro fmap [x y] `(first (filter identity (map ~x ~y))))

(defn lookup-variants [f k]
  (fmap f (string-variants k)))

(defn interpose-keywords [i x]
  (keyword (apply str (interpose i (map name x)))))
