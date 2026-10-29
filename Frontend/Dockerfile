FROM node:20-alpine AS build

WORKDIR /app

# React reads this URL while building the static website.
ARG REACT_APP_API_BASE_URL=/api/todos
ENV REACT_APP_API_BASE_URL=${REACT_APP_API_BASE_URL}

COPY todo/package.json todo/package-lock.json ./
RUN npm ci
COPY todo/ ./
RUN npm run build

FROM nginx:alpine

COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
