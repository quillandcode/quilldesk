# Quilldesk

## Local development

The application runs on port `8080` by default.

### Swagger UI

- `http://localhost:8080/swagger-ui.html`
- Use this URL to explore and test the ticket API endpoints.

### OpenAPI JSON

- `http://localhost:8080/v3/api-docs`

### H2 Console

The H2 database console is exposed at:

- `http://localhost:8080/h2-console`

If you are accessing the app through a forwarded/public Codespaces URL, use the forwarded host plus `/h2-console`.

Example:

- `https://<your-public-url>/h2-console`

If `/h2-console` returns `404`, make sure the application is running and the H2 console servlet is enabled.
