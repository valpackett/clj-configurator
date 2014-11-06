(ns clj-configurator.core-test
  (:use midje.sweet
        clj-configurator.core))

(fact "property lookup works"
  (System/setProperty "TEST_ONE_TWO" "12")
  (:test-one-two props) => "12")

(fact "config works"
  (System/setProperty "HELLO" "you")
  (config :defaults {:hello "world"}) => {:hello "you"})

(fact "config works with nested maps"
  (System/setProperty "DATABASE_URL" "example.com")
  (config :defaults {:database {:url ""}}) => {:database {:url "example.com"}}

  (config :defaults {:database {:url ""}}
          :sources [{:database {:url "test" :thing 1}}])
    => {:database {:url "test"}})

(fact "config works with types"
  (config :defaults {:a 1
                     :b nil
                     :c "hello"}
          :sources [{:a 2
                     :b "b"
                     :c 123}])
    => {:a 2
        :b "b"
        :c "123"})

(fact "rudimentary command line argument (env style) parsing works"
  (let [raw-args ["PORT=3000" 
                  "DEBUG=true" 
                  "PATH=ws/project" 
                  "EMPTY_PERSISTS="
                  "DOUBLE_EQUALS=1+2=3"]]
    (config 
      :sources [(from-args raw-args)] ; pass -main args in normally
      :defaults {:port 9999 
                 :debug false
                 :path ""
                 :empty-persists "default"
                 :double-equals ""
                 }) => {:port 3000
                        :debug true
                        :path "ws/project"
                        :empty-persists "" 
                        :double-equals "1+2=3"
                       }))
