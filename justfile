backend_dir := "e-commerce-backend"
frontend_dir := "e-commerce-frontend"

init:
  cd {{frontend_dir}} && npm install

up:
  cd {{backend_dir}} && ./gradlew bootRun

down:
  docker compose down --volumes --remove-orphans

clean:
  cd {{backend_dir}} && ./gradlew clean
  docker compose down --volumes --remove-orphans --rmi local
