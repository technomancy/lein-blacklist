(ns leiningen.blacklist
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [leiningen.core.main :as main]
            [bultitude.core :as b]))

(defn- require? [[f & _]]
  (#{:require :use} f))

(defn- allowed? [from w]
  (re-find w (name from)))

(defn- blacklist-entry-applies? [from n [b ws]]
  (and (re-find b (name n)) (empty? (filter (partial allowed? from) ws))))

(defn- blacklisted? [from blacklist n]
  (not (empty? (filter (partial blacklist-entry-applies? from n) blacklist))))

(defn- blacklisted [blacklist ns-form]
  (let [from (second ns-form)
        clauses (filter require? (drop 2 ns-form))
        required-nses (for [clause clauses
                            subclause (rest clause)]
                        (if (vector? subclause)
                          (first subclause)
                          subclause))]
    (filter (partial blacklisted? from blacklist) required-nses)))

(defn- get-all-blacklisted-nses [project]
  (mapcat (partial blacklisted (:blacklist project))
          (b/namespace-forms-on-classpath
           :classpath (map io/file (distinct (:source-paths project))))))

(defn blacklist
  "Fail if the project source requires blacklisted namespaces."
  [project]
  (when-let [bad (seq (get-all-blacklisted-nses project))]
    (main/abort "Blacklisted namespaces detected:"
                (string/join ", " (map name bad)))))
