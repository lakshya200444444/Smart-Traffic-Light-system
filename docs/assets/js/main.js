// Main JavaScript for Smart Traffic Management Website
// Updated: 2025-06-22 04:55:10 UTC
// Current User: Alims-Repo (Level 3 Expert)

class TrafficManagementSite {
    constructor() {
        this.currentUser = 'Alims-Repo';
        this.userLevel = 3;
        this.sessionStart = new Date('2025-06-22T16:27:04Z');
        this.currentTime = new Date('2025-06-22T04:55:10Z');

        this.init();
    }

    init() {
        this.setupNavigation();
        this.updateTimestamps();
        this.initializeAnimations();
        this.setupInteractions();

        // Update time every second
        setInterval(() => this.updateTimestamps(), 1000);
    }

    setupNavigation() {
        const navToggle = document.getElementById('nav-toggle');
        const navMenu = document.getElementById('nav-menu');

        if (navToggle && navMenu) {
            navToggle.addEventListener('click', () => {
                navMenu.classList.toggle('active');
                navToggle.classList.toggle('active');
            });
        }

        // Smooth scrolling for anchor links
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });

        // Active navigation highlighting
        this.updateActiveNavigation();
        window.addEventListener('scroll', () => this.updateActiveNavigation());
    }

    updateActiveNavigation() {
        const sections = document.querySelectorAll('section[id]');
        const navLinks = document.querySelectorAll('.nav-link');

        let current = '';
        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            const sectionHeight = section.clientHeight;
            if (window.scrollY >= (sectionTop - 200)) {
                current = section.getAttribute('id');
            }
        });

        navLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === `#${current}`) {
                link.classList.add('active');
            }
        });
    }

    updateTimestamps() {
        // Update current time
        this.currentTime = new Date();
        const timeString = this.currentTime.toISOString().slice(0, 19).replace('T', ' ') + ' UTC';

        // Update all timestamp elements
        const timestampElements = document.querySelectorAll('#current-time, .live-text, [data-timestamp]');
        timestampElements.forEach(element => {
            if (element.id === 'current-time') {
                element.textContent = timeString;
            } else if (element.classList.contains('live-text')) {
                element.textContent = `LIVE: ${timeString}`;
            }
        });

        // Update session duration
        const sessionTime = document.getElementById('session-time');
        if (sessionTime) {
            const duration = this.calculateSessionDuration();
            sessionTime.textContent = duration;
        }
    }

    calculateSessionDuration() {
        const now = this.currentTime;
        const start = this.sessionStart;
        const diff = now - start;

        const hours = Math.floor(diff / (1000 * 60 * 60));
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

        return `${hours}h ${minutes}m`;
    }

    initializeAnimations() {
        // Intersection Observer for scroll animations
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('fade-in');
                }
            });
        }, observerOptions);

        // Observe all feature cards and sections
        document.querySelectorAll('.feature-card, .demo-card, .metric-card').forEach(el => {
            observer.observe(el);
        });

        // Traffic light animation
        this.animateTrafficLights();
    }

    animateTrafficLights() {
        const lights = document.querySelectorAll('.traffic-light');
        if (lights.length === 0) return;

        let currentLight = 2; // Start with green

        setInterval(() => {
            lights.forEach(light => light.classList.remove('active'));

            if (currentLight === 0) { // Red
                lights[0].classList.add('active');
                setTimeout(() => {
                    lights[0].classList.remove('active');
                    lights[1].classList.add('active'); // Yellow
                }, 3000);
                setTimeout(() => {
                    lights[1].classList.remove('active');
                    lights[2].classList.add('active'); // Green
                }, 4000);
                currentLight = 2;
            }
        }, 8000);
    }

    setupInteractions() {
        // Button hover effects
        document.querySelectorAll('.btn').forEach(btn => {
            btn.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-2px)';
            });

            btn.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0)';
            });
        });

        // Feature card interactions
        document.querySelectorAll('.feature-card').forEach(card => {
            card.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-8px)';
            });

            card.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0)';
            });
        });

        // Copy to clipboard functionality
        this.setupCopyButtons();
    }

    setupCopyButtons() {
        document.querySelectorAll('[data-copy]').forEach(button => {
            button.addEventListener('click', async () => {
                const text = button.getAttribute('data-copy');
                try {
                    await navigator.clipboard.writeText(text);
                    this.showNotification('Copied to clipboard!', 'success');
                } catch (err) {
                    this.showNotification('Failed to copy', 'error');
                }
            });
        });
    }

    showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;

        notification.style.cssText = `
      position: fixed;
      top: 20px;
      right: 20px;
      background: ${type === 'success' ? 'var(--secondary-green)' : 'var(--error-red)'};
      color: white;
      padding: 12px 24px;
      border-radius: 6px;
      z-index: 10000;
      transform: translateX(100%);
      transition: transform 0.3s ease;
    `;

        document.body.appendChild(notification);

        // Animate in
        setTimeout(() => {
            notification.style.transform = 'translateX(0)';
        }, 100);

        // Remove after 3 seconds
        setTimeout(() => {
            notification.style.transform = 'translateX(100%)';
            setTimeout(() => {
                document.body.removeChild(notification);
            }, 300);
        }, 3000);
    }

    // Simulate live data updates
    simulateLiveData() {
        const metrics = {
            vehicleCount: 128569,
            systemHealth: 98.7,
            activeIntersections: 247,
            totalIntersections: 250,
            aiEfficiency: 97.1,
            currentFlow: 2847
        };

        // Randomly update metrics slightly
        setInterval(() => {
            metrics.vehicleCount += Math.floor(Math.random() * 10) - 5;
            metrics.systemHealth += (Math.random() - 0.5) * 0.1;
            metrics.aiEfficiency += (Math.random() - 0.5) * 0.2;
            metrics.currentFlow += Math.floor(Math.random() * 100) - 50;

            // Keep values within realistic bounds
            metrics.systemHealth = Math.max(95, Math.min(100, metrics.systemHealth));
            metrics.aiEfficiency = Math.max(90, Math.min(100, metrics.aiEfficiency));
            metrics.currentFlow = Math.max(2000, Math.min(4000, metrics.currentFlow));

            this.updateMetricDisplays(metrics);
        }, 5000);
    }

    updateMetricDisplays(metrics) {
        // Update vehicle count
        const vehicleElement = document.getElementById('vehicle-count');
        if (vehicleElement) {
            vehicleElement.textContent = metrics.vehicleCount.toLocaleString();
        }

        // Update other metrics
        document.querySelectorAll('[data-metric]').forEach(element => {
            const metricName = element.getAttribute('data-metric');
            if (metrics[metricName] !== undefined) {
                if (metricName.includes('percentage') || metricName.includes('health') || metricName.includes('efficiency')) {
                    element.textContent = `${metrics[metricName].toFixed(1)}%`;
                } else {
                    element.textContent = metrics[metricName].toLocaleString();
                }
            }
        });
    }

    // User session management
    getUserInfo() {
        return {
            username: this.currentUser,
            level: this.userLevel,
            sessionStart: this.sessionStart,
            sessionDuration: this.calculateSessionDuration(),
            permissions: this.getUserPermissions()
        };
    }

    getUserPermissions() {
        const permissions = {
            1: ['view_dashboard', 'view_reports'],
            2: ['view_dashboard', 'view_reports', 'manual_control', 'configure_settings'],
            3: ['view_dashboard', 'view_reports', 'manual_control', 'configure_settings', 'emergency_override', 'ai_management', 'system_admin']
        };

        return permissions[this.userLevel] || permissions[1];
    }

    // API simulation
    async makeAPICall(endpoint, method = 'GET', data = null) {
        // Simulate API latency
        await new Promise(resolve => setTimeout(resolve, 200 + Math.random() * 300));

        // Simulate API responses
        const responses = {
            '/api/v1/system/status': {
                timestamp: this.currentTime.toISOString(),
                system_health: 98.7,
                active_intersections: 247,
                total_intersections: 250,
                ai_efficiency: 97.1,
                uptime_days: 47
            },
            '/api/v1/traffic/live': {
                current_flow: 2847,
                average_speed: 29.4,
                congestion_index: 23.7,
                signal_efficiency: 95.3
            },
            '/api/v1/user/session': this.getUserInfo()
        };

        return responses[endpoint] || { success: true, data: 'Mock response' };
    }
}

// Initialize the site when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.trafficSite = new TrafficManagementSite();

    // Start live data simulation
    window.trafficSite.simulateLiveData();
});

// Utility functions
function formatTimestamp(date) {
    return date.toISOString().slice(0, 19).replace('T', ' ') + ' UTC';
}

function formatDuration(milliseconds) {
    const hours = Math.floor(milliseconds / (1000 * 60 * 60));
    const minutes = Math.floor((milliseconds % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours}h ${minutes}m`;
}

function generateRandomData(min, max, decimals = 0) {
    const value = Math.random() * (max - min) + min;
    return decimals > 0 ? parseFloat(value.toFixed(decimals)) : Math.floor(value);
}

// Export for use in other scripts
window.TrafficUtils = {
    formatTimestamp,
    formatDuration,
    generateRandomData
};