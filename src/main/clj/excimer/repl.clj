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
(defonce reflect r/reflect)
(defonce aprint apt/aprint)
(defonce print-stack-trace st/print-stack-trace)
(defonce print-cause-trace st/print-cause-trace)
(defonce print-throwable st/print-throwable)


;; ----------------------------------------------------------------------------
;; Spring
;; ----------------------------------------------------------------------------
(defn get-beans
  "Get the list of beans defined in the application context."
  []
  (into [] (.. NREPLServer/INSTANCE (getApplicationContext) (getBeanDefinitionNames))))

(defn get-bean
  "Get a `bean` from the application context."
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (getBean bean-name)))

(defn has-bean
  "Is the `bean` defined in the application context."
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (containBean bean-name)))

(defn is-singleton
  "Is the `bean` of singleton scope."
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (isSingleton bean-name)))

(defn is-prototype
  "Is the `bean` of prototype scope."
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (isPrototype bean-name)))

(defn get-type
  "Return the java class of the `bean`."
  [^String bean-name]
  (.. NREPLServer/INSTANCE (getApplicationContext) (getType bean-name)))

(defn get-bean-names-from-type
  "Return the beans that are of type `bean-class`."
  [^Class bean-class]
  (into [] (.. NREPLServer/INSTANCE (getApplicationContext) (getBeanNamesForType bean-class))))

(defn get-env
  "Return the environment of the application context."
  []
  (.. NREPLServer/INSTANCE (getApplicationContext) (getEnvironment)))


;; ----------------------------------------------------------------------------
;; Reflection
;; ----------------------------------------------------------------------------
(defn get-methods-info
  "Return the methods information (e.g. return type, arguments, access modifier,
   and etc.) of the given `obj`."
  [obj]
  (filter :return-type (:members (reflect obj))))

(defn get-method-info
  "Same as [[get-methods-info]] except this only returns the information of one
   method."
  [obj ^String fn-name]
  (first (filter (comp (partial = (symbol (str fn-name))) :name)
                 (:members (reflect obj)))))

(defn get-method-names
  "Return a list of method names of the given `obj`."
  [obj]
  (map :name (get-methods-info obj)))
