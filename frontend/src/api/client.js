const API_BASE = "http://localhost:8081";

async function request(url, options = {}) {
  const fullUrl = url.startsWith("http") ? url : `${API_BASE}${url}`;
  const res = await fetch(fullUrl, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options,
  });
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`HTTP ${res.status}: ${text || res.statusText}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export const api = {
    get: (url) => request(url),
    post: (url, body) => request(url, { method: "POST", body: JSON.stringify(body) }),
    patch: (url, body) => request(url, { method: "PATCH", body: JSON.stringify(body) }),
    put: (url, body) => request(url, { method: "PUT", body: JSON.stringify(body) }),
    delete: (url) => request(url, { method: "DELETE" }),
};

export function linkHref(resource, rel) {
  return resource?._links?.[rel]?.href;
}
