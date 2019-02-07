(ns hitch2.transit.tag
  (:require [cognitect.transit :as t]
            [hitch2.descriptor :refer [->Descriptor]])
  (:import (java.io InputStream OutputStream)
           (com.cognitect.transit WriteHandler DefaultReadHandler)
           (hitch2.descriptor Descriptor)))

(def make-pos-dtor
  (reify DefaultReadHandler
    (fromRep [_ tag term]  (->Descriptor (symbol tag) term))))

(def DTORHandler (reify WriteHandler
                   (tag [_ v] (str (:name v)))
                   (rep [_ v] (:term v))
                   (stringRep [_ _] nil)
                   (getVerboseHandler [_] nil)))

(defn reader
  ([^InputStream in type] (reader in type {}))
  ([^InputStream in type opts]
   (t/reader in type (assoc opts :default-handler make-pos-dtor))))

(defn writer
  ([^OutputStream out type] (writer out type {}))
  ([^OutputStream out type opts]
   (t/writer out type (update opts :handlers (fnil assoc {}) Descriptor  DTORHandler) )))
