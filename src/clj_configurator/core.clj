(ns clj-configurator.core
  (:use [clj-configurator util]))

(deftype Env [] clojure.lang.ILookup
  (valAt [_ k] (lookup-variants #(System/getenv %) (name k))))

(def env (Env.))

(deftype Props [] clojure.lang.ILookup
  (valAt [_ k] (lookup-variants #(System/getProperty %) (name k))))

(def props (Props.))

(deftype Args [arg-map] clojure.lang.ILookup
  (valAt [_ k] (lookup-variants #(get arg-map %) (name k))))

(defn from-args [args] 
  (let [arg-map (into {} (map (fn [arg] 
                                (let [parts (.split arg "=" 2) 
                                      arg-key (first parts)
                                      arg-val (or (second parts) "")]
                                  [arg-key arg-val])) args))]
    (Args. arg-map)))

(defn- process-tree [m sources a]
  (into {}
    (map (fn [[k v]]
           (let [path (conj a k)]
             (if (map? v)
               [k (process-tree v sources path)]
               [k (let [r (first (filter (comp not nil?)
                                   [(fmap #(get-in % path) sources)
                                    (fmap #(get-in % (map name path)) sources)
                                    (fmap (interpose-keywords "-" path) sources)
                                    (fmap (interpose-keywords "." path) sources)
                                    v]))]
                    (if (or (nil? v) (instance? (class v) r))
                      r
                      (to-type-of v r)))])))
         m)))

(defn config [& {:keys [defaults sources]
                 :or {sources [props env]}}]
  (process-tree defaults sources []))

(defmacro defconfig [n & body]
  `(def ~n (config ~@body)))
