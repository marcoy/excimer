(ns excimer.class-loading
  (:import [java.net URL URLClassLoader]
           [java.io File]))

(defn- to-url [^String str-path]
  (let [jar-file (File. str-path)]
    (when (.exists jar-file)
      (.. jar-file toURI toURL))))

(defn param-array [& args]
  (into-array Class (map class args)))

(defn load-class
  "Load a class from the given list of jars."
  [^String class-name & paths]
  (let [urls (into-array URL (filter some? (map to-url paths)))
        class-loader (URLClassLoader. urls)]
    (.loadClass class-loader class-name)))

(defn invoke-static-method
  [^Class klass ^String method-name & args]
  (let [arg-classes (apply param-array args)
        method (.getMethod klass method-name arg-classes)]
    (.invoke method nil (into-array args))))

(defn invoke-instance-method [obj ^String method-name & args]
  (let [arg-classes (into-array Class (map class args))
        method (.getMethod obj method-name arg-classes)]
    (.invoke method obj (into-array args))))

(defn invoke-private-method [obj ^String fn-name-string & args]
  (let [m (first (filter (fn [x] (.. x getName (equals fn-name-string)))
                         (.. obj getClass getDeclaredMethods)))]
    (. m (setAccessible true))
    (. m (invoke obj (into-array args)))))
