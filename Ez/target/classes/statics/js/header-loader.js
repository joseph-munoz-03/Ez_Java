/**
 * Script para cargar sidebars y headers dinámicamente desde API
 * Los sidebars se cargan según el rol del usuario y resaltan el menú activo
 */

/**
 * Obtiene el nombre de la página actual basado en la URL
 */
function obtenerPaginaActiva() {
    const pathname = window.location.pathname;
    const partes = pathname.split('/');
    // Retorna la última parte de la URL (ej: 'dashboard', 'chat', etc.)
    return partes[partes.length - 1] || 'dashboard';
}

/**
 * Carga el sidebar del ingeniero dinámicamente
 */
function cargarSidebarIngeniero() {
    const activePage = obtenerPaginaActiva();
    fetch(`/api/headers/sidebar/ingeniero?activePage=${activePage}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('No autorizado');
            }
            return response.text();
        })
        .then(html => {
            const sidebarContainer = document.getElementById('sidebar-container');
            if (sidebarContainer) {
                sidebarContainer.innerHTML = html;
                resaltarNavegacionActiva();
                asignarEventosNavegacion();
            }
        })
        .catch(error => {
            console.error('Error al cargar sidebar del ingeniero:', error);
        });
}

/**
 * Carga el sidebar del usuario/cliente dinámicamente
 */
function cargarSidebarUsuario() {
    const activePage = obtenerPaginaActiva();
    fetch(`/api/headers/sidebar/usuario?activePage=${activePage}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('No autorizado');
            }
            return response.text();
        })
        .then(html => {
            const sidebarContainer = document.getElementById('sidebar-container');
            if (sidebarContainer) {
                sidebarContainer.innerHTML = html;
                resaltarNavegacionActiva();
                asignarEventosNavegacion();
            }
        })
        .catch(error => {
            console.error('Error al cargar sidebar del usuario:', error);
        });
}

/**
 * Carga el sidebar del admin dinámicamente
 */
function cargarSidebarAdmin() {
    const activePage = obtenerPaginaActiva();
    fetch(`/api/headers/sidebar/admin?activePage=${activePage}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('No autorizado');
            }
            return response.text();
        })
        .then(html => {
            const sidebarContainer = document.getElementById('sidebar-container');
            if (sidebarContainer) {
                sidebarContainer.innerHTML = html;
                resaltarNavegacionActiva();
            }
        })
        .catch(error => {
            console.error('Error al cargar sidebar del admin:', error);
        });
}

/**
 * Retorna el header HTML para ingeniero con datos del usuario
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
 * Retorna el header HTML para usuario/cliente con datos del usuario
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
 * Retorna el header HTML para admin con datos del usuario
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
 * Resalta el enlace de navegación activo en el sidebar
 */
function resaltarNavegacionActiva() {
    const activePage = obtenerPaginaActiva();
    const navLinks = document.querySelectorAll('.nav-link[data-page]');
    
    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('data-page') === activePage) {
            link.classList.add('active');
        }
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

// Cargar sidebar y header cuando el documento esté listo
document.addEventListener('DOMContentLoaded', function() {
    // El rol se determinará según la página (ingeniero, usuario, admin, etc.)
    const userRole = document.body.getAttribute('data-user-role');
    
    if (userRole === 'admin') {
        cargarSidebarAdmin();
        cargarHeaderAdmin();
    } else if (userRole === 'ingeniero') {
        cargarSidebarIngeniero();
        cargarHeaderIngeniero();
    } else if (userRole === 'usuario') {
        cargarSidebarUsuario();
        cargarHeaderUsuario();
    }
});
