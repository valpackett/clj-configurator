(ns clj-configurator.core
  (:use [clj-configurator util]))

(deftype Env [] clojure.lang.ILookup
  (valAt [_ k] (lookup-variants #(System/getenv %) (name k))))

(def env (Env.))

(deftype Props [] clojure.lang.ILookup
  (valAt [_ k] (lookup-variants #(System/getProperty %) (name k))))

(def props (Props.))

(defn- process-tree [m sources a]
  (into {}
    (map (fn [[k v]]
           (let [path (conj a k)]
             (if (map? v)
               [k (process-tree v sources path)]
               [k (let [r (or (fmap #(get-in % path) sources)
                              (fmap #(get-in % (map name path)) sources)
                              (fmap (interpose-keywords "-" path) sources)
                              (fmap (interpose-keywords "." path) sources)
                              v)]
                    (if (instance? (class v) r)
                      r
                      (to-type-of v r)))])))
         m)))

(defn config [& {:keys [defaults sources]
                 :or {sources [props env]}}]
  (process-tree defaults sources []))

(defmacro defconfig [n & body]
  `(def ~n (config ~@body)))
