(ns excimer.btrace.commands
  (:import [java.io PrintWriter ObjectInputStream ObjectOutputStream]
           [org.apache.commons.io IOUtils]))


(defmulti readbytes
  "Read raw bytes from the wire and return a map with information about the
   command."
  (fn [c & _] (int c)))

(defmulti writebytes
  "Write the command out in bytes form."
  (fn [c & _] (:type c)))

(defmulti printcmd
  "Pretty print a command. Not all commands support pretty printing."
  (fn [c & _] (:type c)))


;; ----------------------------------------------------------------------------
;; EventCommand
;; ----------------------------------------------------------------------------
(defn event-command
  [^String e]
  {:type 1 :event e})

(defmethod readbytes 1
  [cmd-type ^ObjectInputStream ois]
  (let [ev (.readUTF ois)]
    (event-command ev)))

(defmethod writebytes 1
  [cmd ^ObjectOutputStream oos]
  (let [ev (:event cmd)]
    (.writeByte oos (byte (:type cmd)))
    (.writeUTF oos ev)
    (.flush oos)))


;; ----------------------------------------------------------------------------
;; ErrorCommand
;; ----------------------------------------------------------------------------
(defn error-command
  [cause]
  {:type 0 :cause cause})

(defmethod readbytes 0
  [cmd-type ^ObjectInputStream ois]
  (let [cause (.readObject ois)]
    (error-command cause)))

(defmethod writebytes 0
  [cmd ^ObjectOutputStream oos]
  (let [cause (:cause cmd)]
    (.writeByte oos (byte (:type cmd)))
    (.writeObject oos cause)
    (.flush oos)))


;; ----------------------------------------------------------------------------
;; ExitCommand
;; ----------------------------------------------------------------------------
(defn exit-command
  ([c] {:type 2 :exit-code c})
  ([] (exit-command 0)))

(defmethod readbytes 2
  [cmd-type ois]
  (let [exit-code (.readInt ois)]
    (exit-command exit-code)))

(defmethod writebytes 2
  [cmd oos]
  (do
    (.writeByte oos (byte (:type cmd)))
    (.writeInt oos (:exit-code cmd))
    (.flush oos)))


;; ----------------------------------------------------------------------------
;; InstrumentCommand
;; ----------------------------------------------------------------------------
(defn instrument-command
  [bytecode args]
  {:type 3 :code bytecode :args args})

(defmethod readbytes 3
  [_ ^ObjectInputStream ois]
  (let [len (.readInt ois)
        bytebuffer (byte-array len)
        _ (.readFully ois bytebuffer)
        args (doall (for [_ (range (.readInt ois))] (.readUTF ois)))]
    (instrument-command bytebuffer args)))

; TODO write the arguments
(defmethod writebytes 3
  [cmd ^ObjectOutputStream oos]
  (when-some [bytecode (:code cmd)]
    (do
      (.writeByte oos (byte (:type cmd)))
      (.writeInt oos (count bytecode))
      (.write oos bytecode)
      (.writeInt oos (byte 0))
      (.flush oos))))


;; ----------------------------------------------------------------------------
;; Message
;; ----------------------------------------------------------------------------
(defn message-command
  [t msg]
  {:type 4 :time t :msg msg})

(defmethod readbytes 4
  [cmd-type ^ObjectInputStream ois]
  (let [t (.readLong ois)
        len (.readInt ois)
        bytebuffer (byte-array len)
        bytes-read (.read ois bytebuffer 0 len)
        ; msg (apply str (map char bytebuffer))
        msg (IOUtils/toString bytebuffer)]
    (message-command t msg)))

(defmethod writebytes 4
  [cmd oos])

(defmethod printcmd 4
  [cmd out]
  (let [t (:time cmd)
        msg (:msg cmd)]
    (.print out msg)
    (.flush out)))


;; ----------------------------------------------------------------------------
;; Rename Command
;; ----------------------------------------------------------------------------
(defn rename-command
  [n]
  {:type 5 :new-name n})

(defmethod readbytes 5
  [cmd-type ois]
  (rename-command (.readUTF ois)))

(defmethod writebytes 5
  [cmd oos]
  (do
    (.writeByte oos (byte (:type cmd)))
    (.writeUTF oos (:new-name cmd))
    (.flush oos)))


;; ----------------------------------------------------------------------------
;; Okay
;; ----------------------------------------------------------------------------
(defn okay-command
  []
  {:type 6})

(defmethod readbytes 6
  [cmd-type ois]
  (okay-command))

(defmethod writebytes 6
  [cmd oos])

(defmethod printcmd 6
  [cmd ^PrintWriter out])


;; ----------------------------------------------------------------------------
;; Number Data
;; ----------------------------------------------------------------------------
(defn number-data-command
  ([^String n ^Number v] {:type 9 :name n :value v})
  ([] (number-data-command nil 0)))

(defmethod readbytes 9
  [cmd-type ois]
  (let [n (.readUTF ois)
        v (.readObject ois)]
    (number-data-command n v)))

(defmethod writebytes 9
  [cmd oos]
  (let [n (if-some [maybe-n (:name cmd)] maybe-n "")
        v (:value cmd)]
    (do
      (.writeByte oos (byte (:type cmd)))
      (.writeUTF oos n)
      (.writeObject oos v)
      (.flush oos))))

(defmethod printcmd 9
  [cmd ^PrintWriter out]
  (let [n (:name cmd)
        v (:value cmd)]
    (do
      (when-some [maybe-n n]
        (.print out maybe-n)
        (.print out " = "))
      (.println out v))))


;; ----------------------------------------------------------------------------
;; RetransformationStartNotification
;; ----------------------------------------------------------------------------
(defn retransformation-start-notification
  [n]
  {:type 11 :num-classes n})

(defmethod readbytes 11
  [cmd-type ois]
  (retransformation-start-notification (.readInt ois)))

(defmethod writebytes 11
  [cmd oos]
  (do
    (.writeByte oos (byte (:type cmd)))
    (.writeInt oos (:num-classes cmd))
    (.flush oos)))


;; ----------------------------------------------------------------------------
;; RetransformClassNotification
;; ----------------------------------------------------------------------------
(defn retransform-class-notification
  [class-name]
  {:type 12 :class-name class-name})

(defmethod readbytes 12
  [cmd-type ois]
  (retransform-class-notification (.readObject ois)))

(defmethod writebytes 12
  [cmd oos]
  (do
    (.writeByte oos (byte (:type cmd)))
    (.writeObject oos (:class-name cmd))
    (.flush oos)))


;; ----------------------------------------------------------------------------
;; DEFAULT
;; ----------------------------------------------------------------------------
(defmethod readbytes :default
  [cmd-type ois]
  {:type cmd-type})
