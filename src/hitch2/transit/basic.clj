(ns hitch2.transit.basic
  (:require [cognitect.transit :as t]
            [hitch2.descriptor :refer [->Descriptor]])
  (:import (java.io InputStream OutputStream)
           (com.cognitect.transit WriteHandler TransitFactory ArrayReadHandler ArrayReader)
           (hitch2.descriptor Descriptor)))

(defn curried-dtor []
  (fn [name]
    (fn [term]
      (->Descriptor name term))))

(def make-pos-dtor
  (reify ArrayReadHandler
    (fromRep [_ o] o)
    (arrayReader [_]
      (reify ArrayReader
        (init [_] (curried-dtor))
        (init [_ ^int size] (curried-dtor))
        (add [_ s item]
          (s item))
        (complete [_ s] s)))))

(def DTORHandler (reify WriteHandler
                   (tag [_ v] "DTOR")
                   (rep [_ v] (TransitFactory/taggedValue "array" [(:name v) (:term v)]))
                   (stringRep [_ _] nil)
                   (getVerboseHandler [_] nil)))

(defn reader
  ([^InputStream in type] (reader in type {}))
  ([^InputStream in type opts]
    (t/reader in type (update opts :handlers (fnil assoc {}) "DTOR" make-pos-dtor) )))

(defn writer
  ([^OutputStream out type] (writer out type {}))
  ([^OutputStream out type opts]
    (t/writer out type (update opts :handlers (fnil assoc {}) Descriptor  DTORHandler) )))

(defn make-string-reader [ type opts]
  (let [opts (update opts :handlers (fnil assoc {}) "DTOR" make-pos-dtor)]
    (fn [^InputStream in]
      (t/read (t/reader in type opts)))))

(defn make-string-writer [ type opts]
  (let [opts (update opts :handlers (fnil assoc {}) Descriptor  DTORHandler) ]
    (fn [^OutputStream out]
      (t/writer out type opts))))