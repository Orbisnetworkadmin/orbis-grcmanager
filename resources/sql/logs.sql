-- :name all-logs
-- :doc Devuelve todos los registros del log
SELECT

  log.id_log,
  log.id_user,
  log.date_log,
  log.hostname_log,
  log.resource_log,
  log.severity_log,
  log.message_log

FROM log log;

-- :name insert-log<! :<! :1
-- :doc Inserta un evento en la tabla log
INSERT INTO log
(
  id_user,
  date_log,
  hostname_log,
  resource_log,
  severity_log,
  message_log
) VALUES
(
  :id-user,
  :date-log,
  :hostname-log,
  :resource-log,
  :severity-log,
  :message-log
)
RETURNING id_log;

-- :name delete-all-logs!
-- :doc Borra todos los registros de logs
TRUNCATE TABLE log;