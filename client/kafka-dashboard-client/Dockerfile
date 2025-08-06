# --------------------- BUILD STAGE --------------------------

# Get the latest node image
FROM node:18-alpine AS build
# Set working directory for the container
WORKDIR /client
# Copy packages
COPY package*.json ./
RUN npm install
# Copy the rest of the files
COPY . .
# build the app
RUN npm run build


# ------------------- PROD STAGE ---------------------------
FROM nginx:stable-alpine AS production
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

