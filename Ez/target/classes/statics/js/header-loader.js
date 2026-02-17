/**
 * Script para cargar headers dinámicamente desde API
 * Uso: cargarHeaderIngeniero() o cargarHeaderUsuario()
 */

/**
 * Carga el header del ingeniero dinámicamente
 */
function cargarHeaderIngeniero() {
    fetch('/api/headers/ingeniero')
        .then(response => {
            if (!response.ok) {
                throw new Error('No autorizado');
            }
            return response.text();
        })
        .then(html => {
            const headerContainer = document.getElementById('header-container');
            if (headerContainer) {
                headerContainer.innerHTML = html;
                actualizarTituloPagina();
            }
        })
        .catch(error => {
            console.error('Error al cargar header del ingeniero:', error);
        });
}

/**
 * Carga el header del usuario/cliente dinámicamente
 */
function cargarHeaderUsuario() {
    fetch('/api/headers/usuario')
        .then(response => {
            if (!response.ok) {
                throw new Error('No autorizado');
            }
            return response.text();
        })
        .then(html => {
            const headerContainer = document.getElementById('header-container');
            if (headerContainer) {
                headerContainer.innerHTML = html;
                actualizarTituloPagina();
            }
        })
        .catch(error => {
            console.error('Error al cargar header del usuario:', error);
        });
}

/**
 * Carga el header del admin dinámicamente
 */
function cargarHeaderAdmin() {
    fetch('/api/headers/admin')
        .then(response => {
            if (!response.ok) {
                throw new Error('No autorizado');
            }
            return response.text();
        })
        .then(html => {
            const headerContainer = document.getElementById('header-container');
            if (headerContainer) {
                headerContainer.innerHTML = html;
                actualizarTituloPagina();
            }
        })
        .catch(error => {
            console.error('Error al cargar header del admin:', error);
        });
}

/**
 * Actualiza el título de la página en el header
 * Uso: agregarAtributo data-page-title="Nombre de la Página" al elemento body
 */
function actualizarTituloPagina() {
    const pageTitle = document.body.getAttribute('data-page-title');
    if (pageTitle) {
        const titleElement = document.getElementById('page-title');
        if (titleElement) {
            titleElement.textContent = pageTitle;
        }
    }
}

// Cargar header cuando el documento esté listo
document.addEventListener('DOMContentLoaded', function() {
    // El rol se determinará según la página (ingeniero,usuario,admin, etc.)
    const userRole = document.body.getAttribute('data-user-role');
    
    if (userRole === 'admin') {
        cargarHeaderAdmin();
    } else if (userRole === 'ingeniero') {
        cargarHeaderIngeniero();
    } else if (userRole === 'usuario') {
        cargarHeaderUsuario();
    }
});
