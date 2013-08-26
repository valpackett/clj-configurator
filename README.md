Current [semantic](http://semver.org/) version:

```clojure
[clj-configurator "0.1.4"]
```

# clj-configurator

A powerful yet simple Clojure configuration library.

Supports any configuration format ([TOML](https://github.com/lantiga/clj-toml), [YAML](https://github.com/lancepantz/clj-yaml), [JSON](https://github.com/dakrone/cheshire), EDN, whatever) -- you just parse it yourself.  
Supports environment variables and Java system properties.  
Automatically figures out types based on defaults.

## Usage

```clojure
(ns your.app.config
  (:require [clj-toml.core :as toml]) ; <--- just for example
  (:use clj-configurator.core))

(defconfig settings
  :defaults {:database {:url "postgres://localhost:5432/app"
                        :max-connections 10}
             :log-level :WARN}
  :sources [env props
            (toml/parse-string (slurp "resources/config.toml"))
            ; add json, yaml, whatever parsing here
            ])
```

If you put the following into `resources/config.toml`:

    [database]
    url = "postgres://postgres.heroku.com/aaaaaaaaaaaa"

And set the following environment variables:

```shell
$ export LOG_LEVEL=INFO
$ export DATABASE_MAX_CONNECTIONS=20
```

This is what `settings` will be:

```clojure
{:database {:url "postgres://postgres.heroku.com/aaaaaaaaaaaa"
            :max-connections 20}
 :log-level :INFO}
```

Isn't that awesome?
It converted log-level to a keyword because that's what type was in the default.
Same with max-connections (number).

## License

Copyright © 2013 Greg V <floatboth@me.com>  
This work is free. You can redistribute it and/or modify it under the  
terms of the Do What The Fuck You Want To Public License, Version 2,  
as published by Sam Hocevar. See the COPYING file for more details.
