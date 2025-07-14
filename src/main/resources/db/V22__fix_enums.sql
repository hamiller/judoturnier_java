-- Gruppengröße: "Frauen" → "FRAUEN"
UPDATE einstellungen
SET wert = REPLACE(wert, '"Frauen":', '"FRAUEN":')
WHERE art = 'gruppengroesse' AND wert LIKE '%"Frauen":%';

-- Wettkampfzeit: "Frauen" → "FRAUEN"
UPDATE einstellungen
SET wert = REPLACE(wert, '"Frauen":', '"FRAUEN":')
WHERE art = 'wettkampfzeit' AND wert LIKE '%"Frauen":%';

-- Gruppengröße: "Maenner" → "MAENNER"
UPDATE einstellungen
SET wert = REPLACE(wert, '"Maenner":', '"MAENNER":')
WHERE art = 'gruppengroesse' AND wert LIKE '%"Maenner":%';

-- Wettkampfzeit: "Maenner" → "MAENNER"
UPDATE einstellungen
SET wert = REPLACE(wert, '"Maenner":', '"MAENNER":')
WHERE art = 'wettkampfzeit' AND wert LIKE '%"Maenner":%';
