(ns excimer.repl
  (:require [aprint.core :as apt]
            [clojure.reflect :as r]
            [clojure.repl :refer :all]
            [clojure.stacktrace :as st]
            [clojure.tools.namespace.repl :refer [refresh]]
            [excimer.class-loading :as cl]
            [slingshot.slingshot :refer :all])
  (:import [excimer.nrepl NREPLServer]))


;; ----------------------------------------------------------------------------
;; Aliases
;; ----------------------------------------------------------------------------
(def reflect r/reflect)
(def aprint apt/aprint)
(def print-stack-trace st/print-stack-trace)
(def print-cause-trace st/print-cause-trace)
(def print-throwable st/print-throwable)
(def invoke-static-method cl/invoke-static-method)


;; ----------------------------------------------------------------------------
;; Spring
;; ----------------------------------------------------------------------------
(defn get-beans
  "Get the list of beans defined in the application context."
  []
  (into [] (.. NREPLServer/INSTANCE (getApplicationContext) (getBeanDefinitionNames))))

(defn get-bean
  "Get a bean from the application context."
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (getBean bean-name)))

(defn has-bean
  "Is the bean defined in the application context."
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (containBean bean-name)))

(defn is-singleton
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (isSingleton bean-name)))

(defn is-prototype
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (isPrototype bean-name)))

(defn get-type
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (getType bean-name)))

(defn get-bean-names-from-type
  [^Class bean-class]
  (into [] (.. NREPLServer/INSTANCE (getApplicationContext) (getBeanNamesForType bean-class))))

(defn get-env
  []
  (.. NREPLServer/INSTANCE (getApplicationContext) (getEnvironment)))


;; ----------------------------------------------------------------------------
;; Reflection
;; ----------------------------------------------------------------------------
(defn invoke-private-method [obj ^String fn-name-string & args]
  (let [m (first (filter (fn [x] (.. x getName (equals fn-name-string)))
                         (.. obj getClass getDeclaredMethods)))]
    (. m (setAccessible true))
    (. m (invoke obj (into-array args)))))

(defn get-methods-info [obj]
  (filter :return-type (:members (reflect obj))))

(defn get-method-names [obj]
  (map :name (get-methods-info obj)))

(defn get-method-info [obj ^String fn-name]
  (first (filter (comp (partial = (symbol (str fn-name))) :name)
                 (:members (reflect obj)))))
