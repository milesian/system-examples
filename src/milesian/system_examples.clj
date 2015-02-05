(ns milesian.system-examples
  (:require [com.stuartsierra.component :as component]
            [plumbing.core :refer (fnk sum)]
            [plumbing.graph :as graph]))

(defprotocol Sing
  (singing [_ song]))

(def stats-graph
  "A graph specifying the same computation as 'stats'"
  {:n  (fnk [xs]
            (println :n)
            (count xs))
   :m  (fnk [xs n]
            (println :m)
            (/ (sum identity xs) n))
   :m2 (fnk [xs n]
            (println :m2)
            (/ (sum #(* % %) xs) n))
   :v  (fnk [m m2]
            (println :v)
            (- m2 (* m m)))})

(def stats-eager (graph/compile stats-graph))

(def singing-g
  "A graph specifying the same computation as 'stats'"
  {:sing  (fnk [lyrics]
               (-> lyrics clojure.string/upper-case seq))})

(def p-singing
  (reify Sing
   (singing [this song]
     (:sing ((graph/compile singing-g) {:lyrics song}))
     )))

(defn- create-state [k]
  {:state (str "state " k ": "  (rand-int Integer/MAX_VALUE))})

(defprotocol Listen
  (listening [_]))

(defprotocol Talk
  (talking [_]))

(defrecord ComponentA [state g s-g]
  component/Lifecycle
  (start [this]
    (println "starting A")
    (let [i (assoc this
             :g (graph/compile g)
             :s-g (graph/compile s-g))]
      (extend (type this)
        Sing
        {:singing (fn [this song]
                    (:sing ((-> this :s-g) {:lyrics (str "guau!!! " song)})))

         })
      #_(reify Sing
        (singing [this song]
          (:sing ((-> i :s-g) {:lyrics song})))
        )
      i

      ))
  (stop [this]
    this)


  )

(defn component-a [] (map->ComponentA  {:state "juan" :g stats-graph :s-g singing-g}))

(defrecord ComponentB [state a]
  component/Lifecycle
  (start [this]
    this)
  (stop [this]
    this)

  Listen
  (listening [this]
    "I'm component :b, listening now ..."))

(defn component-b [] (map->ComponentB (create-state :b)))

(defrecord ComponentC [state a b]
  Talk
  (talking [this]
    (str state "I'm :c, I'm talking and now listening to :b " (listening b)))
  )

(defn component-c [] (map->ComponentC (create-state :c)))

(defrecord System1 [a b c])

(defn new-system-map []
  (component/system-map :a (-> (component-a))
                        :b (-> (component-b)
                               (component/using [:a]))
                        :c (-> (component-c)
                               (component/using [:a :b]))))
(def s (component/start (new-system-map)))

(time (-> s :a (singing "la ala la ")) )
(assert  (satisfies? Sing p-singing))
(assert  (satisfies? Sing (-> s :a)))
;(singing p-singing "hola")
