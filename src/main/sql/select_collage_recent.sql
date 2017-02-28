USE lifecollage_00_01_00;

SET @user_id := 85;

SELECT
  c.id                  AS collage_id,
  c.title               AS collage_title,
  c.created             AS collage_created,
  c.application_user_id AS collage_application_user_id,

  p.id                  AS picture_id,
  p.location            AS picture_title,
  p.created             AS picture_created

FROM
  collage AS c
  LEFT JOIN (
              SELECT
                collage_id,
                MAX(id)      AS id,
                MAX(created) AS created
              FROM picture
              WHERE collage_id IN (SELECT id
                                   FROM collage
                                   WHERE application_user_id = @user_id)
              GROUP BY collage_id
            ) AS mp ON c.id = mp.collage_id
  LEFT JOIN (
              SELECT *
              FROM picture
              WHERE collage_id IN (SELECT id
                                   FROM collage
                                   WHERE application_user_id = @user_id)
            ) AS p ON mp.id = p.id

WHERE
  application_user_id = @user_id

GROUP BY
  c.id

ORDER BY
  picture_created DESC
