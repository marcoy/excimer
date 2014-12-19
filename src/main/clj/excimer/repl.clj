(ns excimer.repl
  (:require [aprint.core :refer :all]
            [clojure.reflect :refer [reflect]]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :refer [refresh]]
            [slingshot.slingshot :refer :all])
  (:import [excimer.nrepl NREPLServer]))


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
