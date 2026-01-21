# Use a lightweight Nginx image
FROM nginx:alpine

# Copy static files to the Nginx server's public directory
COPY . /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# The default Nginx entrypoint will start the server
