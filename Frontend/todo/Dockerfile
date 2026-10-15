FROM node:20-alpine

WORKDIR /app

# 1. Copy package files and install dependencies first (cached unless package.json changes)
COPY package*.json ./
RUN npm install

# 2. Copy the rest of your source code
COPY . .

EXPOSE 3000

CMD ["npm", "start"]