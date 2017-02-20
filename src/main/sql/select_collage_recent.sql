USE lifecollage_00_01_00;
SELECT
  c.id                  AS collage_id,
  c.title               AS collage_title,
  c.created             AS collage_created,
  c.application_user_id AS collage_application_user_id,

  p.id                  AS picture_id,
  p.location            AS picture_title,
  MAX(p.created)        AS picture_created

FROM
  collage AS c
  LEFT JOIN picture AS p ON c.id = p.collage_id

WHERE
  application_user_id = 85

GROUP BY
  c.id

ORDER BY
  picture_created DESC
