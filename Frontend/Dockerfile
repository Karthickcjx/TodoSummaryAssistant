FROM node:20-alpine AS build

WORKDIR /app

# React reads this URL while building the static website.
ARG REACT_APP_API_BASE_URL=http://localhost:8080/api/todos
ENV REACT_APP_API_BASE_URL=${REACT_APP_API_BASE_URL}

RUN apk add --no-cache git
RUN git clone https://github.com/Karthickcjx/TodoSummaryAssistant.git .

WORKDIR /app/Frontend/todo
RUN npm install
RUN npm run build

FROM nginx:alpine

COPY --from=build /app/Frontend/todo/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
