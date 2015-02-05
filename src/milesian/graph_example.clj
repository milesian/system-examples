(ns milesian.graph-example
  (:require [com.stuartsierra.component :as component]
            [plumbing.core :refer (fnk sum)]
            [plumbing.graph :as graph]))

(defprotocol Singer
  (sing [_ song]))

(def singer-graph
  {:sing  (fnk [lyrics]
               (-> lyrics clojure.string/upper-case seq))})

(defrecord ComponentA [singer-graph]
  component/Lifecycle
  (start [this]
    (println "starting A")
    (assoc this
      :singer-graph (graph/compile singer-graph)))
  (stop [this]
    this)
  Singer
  (sing [this song]
    (:sing (singer-graph {:lyrics song}))))


(sing (component/start (ComponentA. singer-g)) "lalalalaaaaá")
;;=>  (\L \A \L \A \L \A \L \A \A \A \A \Á)


;; not so loud, please
(sing (component/start (ComponentA.
                        (assoc singer-g :sing
                               (fnk [lyrics]
                                    (-> lyrics clojure.string/lower-case seq)))))
      "lalalalaaaaá")
;;=> (\l \a \l \a \l \a \l \a \a \a \a \á)
