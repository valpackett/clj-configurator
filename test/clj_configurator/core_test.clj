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
  (config :defaults {:a 1}
          :sources [{:a 2}]) => {:a 2})
