(ns clj-configurator.util-test
  (:use midje.sweet
        clj-configurator.util))

(fact "to-type-of converts types"
  (to-type-of 1 "123") => 123
  (to-type-of 1.0 "12.34") => 12.34
  (to-type-of true "true") => true
  (to-type-of true "false") => false
  (to-type-of :kw "hello") => :hello)

(fact "string-variants creates different variants"
  (string-variants "hello-world")
    => ["hello-world" "HELLO-WORLD" "hello_world" "HELLO_WORLD"])
