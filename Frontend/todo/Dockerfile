# Compile the React app.
FROM node:22-alpine AS build
WORKDIR /workspace

COPY package*.json ./
RUN npm ci

COPY . .
ARG REACT_APP_API_BASE_URL=http://localhost:8080/api
ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL
RUN npm run build

# Serve static files with an unprivileged Nginx image.
FROM nginxinc/nginx-unprivileged:1.27-alpine AS runtime
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/build /usr/share/nginx/html
EXPOSE 8080
