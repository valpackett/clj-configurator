(ns clj-configurator.util
  (:require [clojure.string :as string]))

(defprotocol Typeable
  (to-type-of [this x]))

(extend-protocol Typeable
  nil     (to-type-of [this x] x)
  String  (to-type-of [this x] (str x))
  Long    (to-type-of [this x] (Long/parseLong x))
  Integer (to-type-of [this x] (Integer/parseInt x))
  Double  (to-type-of [this x] (Double/parseDouble x))
  Float   (to-type-of [this x] (Float/parseFloat x))
  Boolean (to-type-of [this x] (boolean (#{true "true" "True" "TRUE" 1 "1" "yes" "on"} x)))
  clojure.lang.Keyword (to-type-of [this x] (keyword x)))

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
