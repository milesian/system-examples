# system-examples

Just a few stuartsierra system definition examples to play with :)


#### Releases and Dependency Information


```clojure
[milesian/system-examples "0.1.1-SNAPSHOT"]
```

```clojure
:dependencies [[org.clojure/clojure "1.6.0"]
               [com.stuartsierra/component "0.2.2"]]
```


## Recomended usage

Declare this dependency in your project.clj in :profiles :dev section
```clojure 
:profiles {:dev {:dependencies [[milesian/system-examples "0.1.1-SNAPSHOT"]]}}
```
If you are using repl
```clojure
REPL> (in-ns 'the-ns-where-you-are-currently-working)
the-ns-you-are-currently-working> (use 'milesian.system-examples)
the-ns-you-are-currently-working> (new-system-map)
;;=> #milesian.system_examples.System1{:a #milesian.system_examples.ComponentA{:state {:state "state :a: 5404919"}}, :b #milesian.system_examples.ComponentB{:state "state :b: 158819547", :a nil}, :c #milesian.system_examples.ComponentC{:state "state :c: 1596205639", :a nil, :b nil}}

```

## License

Copyright © 2014 Juan Antonio Ruz 

Distributed under the [MIT License](http://opensource.org/licenses/MIT). This means that pieces of this library may be copied into other libraries if they don't wish to have this as an explicit dependency, as long as it is credited within the code.

