(ns orbis-grcmanager.routes.services.attachments
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :refer :all]
            [orbis-grcmanager.layout :refer [error-page]]
            [orbis-grcmanager.db.core :as db]
            [orbis-grcmanager.routes.services.common :refer [handler]]
            [schema.core :as s])
  (:import [java.io ByteArrayOutputStream
                    ByteArrayInputStream
                    FileInputStream]))

(def AttachmentResult
  {(s/optional-key :name)  s/Str
   (s/optional-key :error) s/Str})

(defn file->byte-array [x]
  (with-open [input  (FileInputStream. x)
              buffer (ByteArrayOutputStream.)]
    (clojure.java.io/copy input buffer)
    (.toByteArray buffer)))

(handler attach-file-to-issue! [support-issue-id {:keys [tempfile filename content-type]}]
  (if
    (empty? filename)
    (bad-request "Debe seleccionar un archivo")
    (let [db-file-name (.replaceAll filename "[^a-zA-Z0-9-_\\.]" "")]
      (db/save-file! {:support-issue-id support-issue-id
                      :type             content-type
                      :name             db-file-name
                      :data             (file->byte-array tempfile)})
      (ok {:name db-file-name}))))

(handler remove-file-from-issue! [opts]
  (ok (db/delete-file<! opts)))

(handler load-file-data [file]
  (if-let [{:keys [type data]} (db/load-file-data file)]
    (-> (ByteArrayInputStream. data)
        (ok)
        (content-type type))
    (error-page {:status 404
                 :title  "Archivo no encontrado"})))
