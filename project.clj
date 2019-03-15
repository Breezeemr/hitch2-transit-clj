(defproject com.breezeehr/hitch2-transit-clj "0.3.1-SNAPSHOT"
  :description ""
  :url "https://github.com/Breezeemr/hitch2/tree/master/"
  :repositories [["snapshots" {:url "s3p://breezepackages/snapshots" :creds :gpg}]
                 ["releases" {:url "s3p://breezepackages/releases" :creds :gpg}]]
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.cognitect/transit-clj "0.8.313"]
                 [com.breezeehr/hitch2 "0.3.2-SNAPSHOT"
                  :exclusions [com.clojure/clojurescript]]])
