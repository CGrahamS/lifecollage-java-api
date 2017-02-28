USE lifecollage_00_01_00;
SELECT
  collage_id,
  MAX(id)      AS id,
  location,
  MAX(created) AS created
FROM picture
WHERE collage_id = 62
GROUP BY collage_id;


SELECT *
FROM picture
WHERE collage_id = 62
ORDER BY created DESC;
