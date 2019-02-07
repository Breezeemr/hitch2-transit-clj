(ns hitch2.transit.basic
  (:require [cognitect.transit :as t]
            [hitch2.descriptor :refer [->Descriptor]])
  (:import (java.io InputStream OutputStream)
           (com.cognitect.transit WriteHandler TransitFactory ArrayReadHandler ArrayReader)
           (hitch2.descriptor Descriptor)))

(def make-pos-dtor
  (reify ArrayReadHandler
    (fromRep [_ o] o)
    (arrayReader [_]
      (reify ArrayReader
        (init [_] nil)
        (init [_ ^int size] nil)
        (add [_ s item] (if (nil? s)
                          (->Descriptor item nil)
                          (assoc s :term item)))
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
