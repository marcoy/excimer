(ns excimer.class-loading
  (:import [java.net URL URLClassLoader]
           [java.io File]))

(defn- to-url [^String str-path]
  (let [jar-file (File. str-path)]
    (when (.exists jar-file)
      (.. jar-file toURI toURL))))

(defn load-class [^String class-name & paths]
  (let [urls (into-array URL (filter some? (map to-url paths)))
        class-loader (URLClassLoader. urls)]
    (.loadClass class-loader class-name)))

(defn invoke-static-method [^Class klass ^String method-name & args]
  (let [arg-classes (into-array Class (map class args))
        method (.getMethod klass method-name arg-classes)]
    (.invoke method nil (into-array args))))

(defn invoke-instance-method [obj ^String method-name & args]
  (let [arg-classes (into-array Class (map class args))
        method (.getMethod obj method-name arg-classes)]
    (.invoke method obj (into-array args))))
