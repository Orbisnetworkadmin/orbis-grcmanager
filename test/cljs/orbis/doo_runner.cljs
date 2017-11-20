(ns orbis-grcmanager.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [orbis-grcmanager.core-test]))

(doo-tests 'orbis-grcmanager.core-test)

