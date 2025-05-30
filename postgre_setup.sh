docker run --name shareit-postgres \
-e POSTGRES_USER=shareit \
-e POSTGRES_PASSWORD=shareit \
-e POSTGRES_DB=shareit \
-p 5432:5432 \
-v shareit-data:/var/lib/postgresql/shareit/data \
-d postgres