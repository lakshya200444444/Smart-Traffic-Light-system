// Demo Page JavaScript
// Updated: 2025-06-22 05:13:04 UTC
// Current User: Alims-Repo (Level 3 Expert)

class TrafficDemo {
    constructor() {
        this.currentUser = 'Alims-Repo';
        this.userLevel = 3;
        this.sessionStart = new Date('2025-06-22T16:27:04Z');
        this.currentTime = new Date('2025-06-22T05:13:04Z');
        this.isEmergencyActive = false;
        this.currentTab = 'overview';

        // Demo data
        this.demoData = {
            systemHealth: 98.7,
            activeIntersections: 247,
            totalIntersections: 250,
            aiEfficiency: 97.1,
            currentFlow: 2847,
            vehicleCount: 128569,
            averageSpeed: 29.4,
            congestionIndex: 23.7,
            signalEfficiency: 95.3
        };

        this.init();
    }

    init() {
        this.setupTabSwitching();
        this.generateIntersectionMap();
        this.initializeCharts();
        this.setupInteractions();
        this.startDataSimulation();
        this.updateTimestamps();

        // Update every 30 seconds
        setInterval(() => this.simulateDataUpdates(), 30000);
        setInterval(() => this.updateTimestamps(), 1000);
    }

    setupTabSwitching() {
        const tabButtons = document.querySelectorAll('[data-view]');
        const tabContents = document.querySelectorAll('.demo-tab');

        tabButtons.forEach(button => {
            button.addEventListener('click', (e) => {
                const targetView = e.target.getAttribute('data-view');

                // Update button states
                tabButtons.forEach(btn => btn.classList.remove('active'));
                e.target.classList.add('active');

                // Update tab content
                tabContents.forEach(tab => tab.classList.remove('active'));
                const targetTab = document.getElementById(`${targetView}-tab`);
                if (targetTab) {
                    targetTab.classList.add('active');
                    this.currentTab = targetView;

                    // Initialize tab-specific features
                    this.initializeTabFeatures(targetView);
                }
            });
        });
    }

    initializeTabFeatures(tabName) {
        switch(tabName) {
            case 'monitoring':
                this.updateIntersectionMap();
                break;
            case 'ai':
                this.updateAIMetrics();
                break;
            case 'emergency':
                this.updateEmergencyStatus();
                break;
            default:
                this.updateOverviewMetrics();
        }
    }

    generateIntersectionMap() {
        const grid = document.querySelector('.intersection-grid');
        if (!grid) return;

        // Clear existing points
        grid.innerHTML = '';

        // Generate 48 intersection points (8x6 grid)
        for (let i = 0; i < 48; i++) {
            const point = document.createElement('div');
            point.className = 'intersection-point';

            // Randomly assign status
            const statuses = ['optimal', 'good', 'warning', 'critical'];
            const weights = [0.6, 0.25, 0.12, 0.03]; // Most are optimal
            const status = this.weightedRandom(statuses, weights);
            point.classList.add(status);

            // Add click handler
            point.addEventListener('click', () => {
                this.showIntersectionDetails(i, status);
            });

            // Add tooltip
            point.title = `Intersection ${i + 1}: ${status.toUpperCase()}`;

            grid.appendChild(point);
        }
    }

    weightedRandom(items, weights) {
        const random = Math.random();
        let cumulative = 0;

        for (let i = 0; i < items.length; i++) {
            cumulative += weights[i];
            if (random < cumulative) {
                return items[i];
            }
        }

        return items[items.length - 1];
    }

    showIntersectionDetails(id, status) {
        const modal = this.createModal();
        const details = this.generateIntersectionDetails(id, status);

        modal.innerHTML = `
      <div class="modal-content">
        <div class="modal-header">
          <h3>Intersection ${id + 1} Details</h3>
          <button class="modal-close">&times;</button>
        </div>
        <div class="modal-body">
          ${details}
        </div>
      </div>
    `;

        document.body.appendChild(modal);

        // Close modal handlers
        modal.querySelector('.modal-close').addEventListener('click', () => {
            document.body.removeChild(modal);
        });

        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                document.body.removeChild(modal);
            }
        });
    }

    generateIntersectionDetails(id, status) {
        const streetNames = [
            'Main St & 5th Ave', 'Oak St & 3rd Ave', 'Broadway & 1st St',
            'Park Ave & 7th St', 'Center St & 2nd Ave', 'Pine St & 4th Ave'
        ];

        const streetName = streetNames[id % streetNames.length];
        const flowRate = Math.floor(Math.random() * 200) + 50;
        const waitTime = Math.floor(Math.random() * 45) + 15;
        const efficiency = Math.floor(Math.random() * 20) + 80;

        return `
      <div class="intersection-details">
        <div class="detail-row">
          <span class="label">Location:</span>
          <span class="value">${streetName}</span>
        </div>
        <div class="detail-row">
          <span class="label">Status:</span>
          <span class="value status-${status}">${status.toUpperCase()}</span>
        </div>
        <div class="detail-row">
          <span class="label">Flow Rate:</span>
          <span class="value">${flowRate} vehicles/hour</span>
        </div>
        <div class="detail-row">
          <span class="label">Average Wait:</span>
          <span class="value">${waitTime} seconds</span>
        </div>
        <div class="detail-row">
          <span class="label">Efficiency:</span>
          <span class="value">${efficiency}%</span>
        </div>
        <div class="detail-actions">
          <button class="btn btn-primary" onclick="window.trafficDemo.optimizeIntersection(${id})">
            Optimize Now
          </button>
          <button class="btn btn-secondary" onclick="window.trafficDemo.manualControl(${id})">
            Manual Control
          </button>
        </div>
      </div>
    `;
    }

    createModal() {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.style.cssText = `
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.8);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 10000;
      animation: fadeIn 0.3s ease;
    `;

        return modal;
    }

    initializeCharts() {
        const canvas = document.getElementById('traffic-chart');
        if (!canvas) return;

        const ctx = canvas.getContext('2d');
        const width = canvas.width;
        const height = canvas.height;

        // Clear canvas
        ctx.clearRect(0, 0, width, height);

        // Generate traffic flow data
        const dataPoints = 24; // 24 hours
        const data = [];

        for (let i = 0; i < dataPoints; i++) {
            // Simulate daily traffic pattern
            const hour = i;
            let baseFlow = 2000;
            // Demo Page JavaScript (continued)
// Updated: 2025-06-22 05:16:07 UTC
// Current User: Alims-Repo (Level 3 Expert)

            // Rush hour patterns
            if (hour >= 7 && hour <= 9) {
                baseFlow += 1200; // Morning rush
            } else if (hour >= 17 && hour <= 19) {
                baseFlow += 1500; // Evening rush
            } else if (hour >= 12 && hour <= 14) {
                baseFlow += 600; // Lunch peak
            } else if (hour >= 22 || hour <= 5) {
                baseFlow -= 800; // Night reduction
            }

            // Add random variation
            const variation = (Math.random() - 0.5) * 400;
            data.push(Math.max(800, baseFlow + variation));
        }

        this.drawTrafficChart(ctx, data, width, height);
    }

    drawTrafficChart(ctx, data, width, height) {
        const padding = 40;
        const chartWidth = width - (padding * 2);
        const chartHeight = height - (padding * 2);

        // Set up styling
        ctx.strokeStyle = '#2196F3';
        ctx.lineWidth = 2;
        ctx.fillStyle = 'rgba(33, 150, 243, 0.1)';

        // Draw grid
        ctx.strokeStyle = '#30363D';
        ctx.lineWidth = 1;

        // Vertical grid lines
        for (let i = 0; i <= 6; i++) {
            const x = padding + (i * chartWidth / 6);
            ctx.beginPath();
            ctx.moveTo(x, padding);
            ctx.lineTo(x, height - padding);
            ctx.stroke();
        }

        // Horizontal grid lines
        for (let i = 0; i <= 4; i++) {
            const y = padding + (i * chartHeight / 4);
            ctx.beginPath();
            ctx.moveTo(padding, y);
            ctx.lineTo(width - padding, y);
            ctx.stroke();
        }

        // Draw chart line
        ctx.strokeStyle = '#2196F3';
        ctx.lineWidth = 3;
        ctx.beginPath();

        const maxValue = Math.max(...data);
        const minValue = Math.min(...data);
        const range = maxValue - minValue;

        data.forEach((value, index) => {
            const x = padding + (index * chartWidth / (data.length - 1));
            const y = height - padding - ((value - minValue) / range * chartHeight);

            if (index === 0) {
                ctx.moveTo(x, y);
            } else {
                ctx.lineTo(x, y);
            }
        });

        ctx.stroke();

        // Fill area under curve
        ctx.lineTo(width - padding, height - padding);
        ctx.lineTo(padding, height - padding);
        ctx.closePath();
        ctx.fill();

        // Add labels
        ctx.fillStyle = '#E6EDF3';
        ctx.font = '12px Inter';
        ctx.textAlign = 'center';

        // Time labels
        const timeLabels = ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00'];
        timeLabels.forEach((label, index) => {
            const x = padding + (index * chartWidth / 6);
            ctx.fillText(label, x, height - 10);
        });

        // Current time indicator
        const currentHour = new Date().getHours();
        const currentX = padding + (currentHour * chartWidth / 24);
        ctx.strokeStyle = '#4CAF50';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.moveTo(currentX, padding);
        ctx.lineTo(currentX, height - padding);
        ctx.stroke();

        // Current value indicator
        const currentValue = data[currentHour] || this.demoData.currentFlow;
        const currentY = height - padding - ((currentValue - minValue) / range * chartHeight);
        ctx.fillStyle = '#4CAF50';
        ctx.beginPath();
        ctx.arc(currentX, currentY, 4, 0, 2 * Math.PI);
        ctx.fill();
    }

    setupInteractions() {
        // Emergency button handlers
        window.activateEmergency = (type) => {
            this.activateEmergencyMode(type);
        };

        // AI control handlers
        window.triggerOptimization = () => {
            this.triggerAIOptimization();
        };

        window.pauseAI = () => {
            this.pauseAISystem();
        };

        window.resetModels = () => {
            this.resetAIModels();
        };

        // Refresh stats handler
        window.refreshStats = () => {
            this.refreshLiveStats();
        };

        // Intersection interaction handlers
        window.trafficDemo = this; // Make demo instance globally available
    }

    activateEmergencyMode(type) {
        if (this.userLevel < 3) {
            this.showNotification('Insufficient privileges for emergency control', 'error');
            return;
        }

        this.isEmergencyActive = true;

        // Update emergency status
        const statusElement = document.querySelector('.emergency-status');
        if (statusElement) {
            statusElement.className = 'emergency-status active';
            statusElement.innerHTML = `
        <span class="status-dot"></span>
        <span>EMERGENCY ACTIVE - ${type.replace('-', ' ').toUpperCase()}</span>
      `;
        }

        // Create emergency route
        this.createEmergencyRoute(type);

        // Log emergency action
        this.logEmergencyAction(type);

        this.showNotification(`Emergency ${type.replace('-', ' ')} activated`, 'warning');

        // Auto-deactivate after 5 minutes (demo)
        setTimeout(() => {
            this.deactivateEmergencyMode();
        }, 300000);
    }

    createEmergencyRoute(type) {
        const routeList = document.getElementById('emergency-route-list');
        const noRoutes = document.querySelector('.no-routes');
        const routeCount = document.querySelector('.route-count');

        if (!routeList) return;

        const routes = {
            'all-stop': {
                name: 'All Stop Protocol',
                location: 'City-wide',
                priority: 'CRITICAL',
                duration: '5:00'
            },
            'priority-route': {
                name: 'Emergency Vehicle Corridor',
                location: 'Main St to Hospital',
                priority: 'HIGH',
                duration: '3:30'
            },
            'manual-override': {
                name: 'Manual Control Active',
                location: 'Selected Intersections',
                priority: 'MEDIUM',
                duration: '10:00'
            },
            'evacuation': {
                name: 'Evacuation Mode',
                location: 'Downtown District',
                priority: 'CRITICAL',
                duration: '15:00'
            }
        };

        const route = routes[type];
        if (!route) return;

        const routeElement = document.createElement('div');
        routeElement.className = 'emergency-route-item';
        routeElement.innerHTML = `
      <div class="route-header">
        <h4>${route.name}</h4>
        <span class="route-priority ${route.priority.toLowerCase()}">${route.priority}</span>
      </div>
      <div class="route-details">
        <span class="route-location">${route.location}</span>
        <span class="route-duration">Duration: ${route.duration}</span>
        <span class="route-time">Started: ${this.formatTime(new Date())}</span>
      </div>
      <div class="route-actions">
        <button class="btn btn-secondary btn-sm" onclick="window.trafficDemo.modifyRoute('${type}')">
          Modify
        </button>
        <button class="btn btn-error btn-sm" onclick="window.trafficDemo.cancelRoute('${type}')">
          Cancel
        </button>
      </div>
    `;

        routeList.appendChild(routeElement);

        if (noRoutes) noRoutes.style.display = 'none';
        routeList.style.display = 'block';

        if (routeCount) {
            const currentCount = routeList.children.length;
            routeCount.textContent = `${currentCount} Active`;
        }
    }

    deactivateEmergencyMode() {
        this.isEmergencyActive = false;

        const statusElement = document.querySelector('.emergency-status');
        if (statusElement) {
            statusElement.className = 'emergency-status normal';
            statusElement.innerHTML = `
        <span class="status-dot"></span>
        <span>NORMAL OPERATIONS</span>
      `;
        }

        // Clear emergency routes
        const routeList = document.getElementById('emergency-route-list');
        const noRoutes = document.querySelector('.no-routes');
        const routeCount = document.querySelector('.route-count');

        if (routeList) {
            routeList.innerHTML = '';
            routeList.style.display = 'none';
        }

        if (noRoutes) noRoutes.style.display = 'block';
        if (routeCount) routeCount.textContent = '0 Active';

        this.showNotification('Emergency mode deactivated', 'success');
    }

    logEmergencyAction(type) {
        const historyList = document.querySelector('.history-list');
        if (!historyList) return;

        const actionNames = {
            'all-stop': 'All Stop Activated',
            'priority-route': 'Priority Route Created',
            'manual-override': 'Manual Override',
            'evacuation': 'Evacuation Mode'
        };

        const locations = {
            'all-stop': 'City-wide',
            'priority-route': 'Main St to Hospital',
            'manual-override': 'Multiple Intersections',
            'evacuation': 'Downtown District'
        };

        const historyItem = document.createElement('div');
        historyItem.className = 'history-item active';
        historyItem.innerHTML = `
      <div class="history-time">${this.formatTime(new Date())}</div>
      <div class="history-action">${actionNames[type]}</div>
      <div class="history-location">${locations[type]}</div>
      <div class="history-duration">Duration: Active</div>
      <div class="history-user">By: ${this.currentUser}</div>
    `;

        // Insert at the beginning
        historyList.insertBefore(historyItem, historyList.firstChild);

        // Remove oldest items if more than 5
        while (historyList.children.length > 5) {
            historyList.removeChild(historyList.lastChild);
        }
    }

    triggerAIOptimization() {
        this.showNotification('AI optimization triggered for all intersections', 'info');

        // Simulate optimization process
        const optimizationList = document.querySelector('.optimization-list');
        if (optimizationList) {
            const optimization = document.createElement('div');
            optimization.className = 'optimization-item';
            optimization.innerHTML = `
        <div class="optimization-info">
          <h4>City-wide Optimization</h4>
          <p>All intersections</p>
        </div>
        <div class="optimization-progress">
          <div class="progress-circle" data-progress="0">
            <span>0%</span>
          </div>
          <span class="eta">Calculating...</span>
        </div>
      `;

            optimizationList.insertBefore(optimization, optimizationList.firstChild);

            // Animate progress
            this.animateOptimizationProgress(optimization);
        }

        // Update AI metrics slightly
        this.demoData.aiEfficiency = Math.min(100, this.demoData.aiEfficiency + 0.5);
        this.updateAIMetrics();
    }

    animateOptimizationProgress(element) {
        const progressCircle = element.querySelector('.progress-circle');
        const progressText = progressCircle.querySelector('span');
        const etaElement = element.querySelector('.eta');

        let progress = 0;
        const duration = 15000; // 15 seconds
        const interval = 100; // Update every 100ms
        const increment = (100 / (duration / interval));

        const timer = setInterval(() => {
            progress += increment;

            if (progress >= 100) {
                progress = 100;
                progressText.textContent = '100%';
                etaElement.textContent = 'Complete';
                element.classList.add('completed');
                clearInterval(timer);

                // Remove after 3 seconds
                setTimeout(() => {
                    if (element.parentNode) {
                        element.parentNode.removeChild(element);
                    }
                }, 3000);
            } else {
                progressText.textContent = `${Math.floor(progress)}%`;
                const remainingTime = Math.ceil((100 - progress) * (duration / 100) / 1000);
                etaElement.textContent = `${remainingTime}s remaining`;
            }

            progressCircle.setAttribute('data-progress', Math.floor(progress));
        }, interval);
    }

    pauseAISystem() {
        this.showNotification('AI system paused for maintenance', 'warning');

        // Update AI status
        const aiIndicator = document.querySelector('.ai-indicator');
        if (aiIndicator) {
            aiIndicator.className = 'ai-indicator paused';
            aiIndicator.innerHTML = `
        <span class="pulse-dot" style="background-color: #FF9800;"></span>
        <span>AI PAUSED</span>
      `;
        }

        // Resume after 30 seconds (demo)
        setTimeout(() => {
            if (aiIndicator) {
                aiIndicator.className = 'ai-indicator active';
                aiIndicator.innerHTML = `
          <span class="pulse-dot"></span>
          <span>AI ACTIVE</span>
        `;
            }
            this.showNotification('AI system resumed operation', 'success');
        }, 30000);
    }

    resetAIModels() {
        if (this.userLevel < 3) {
            this.showNotification('Insufficient privileges to reset AI models', 'error');
            return;
        }

        // Show confirmation dialog
        if (confirm('Are you sure you want to reset all AI models? This will temporarily reduce system efficiency.')) {
            this.showNotification('AI models reset - retraining in progress', 'info');

            // Temporarily reduce efficiency
            this.demoData.aiEfficiency = 85.0;
            this.updateAIMetrics();

            // Gradually restore efficiency over 2 minutes
            const restoreInterval = setInterval(() => {
                if (this.demoData.aiEfficiency < 97.1) {
                    this.demoData.aiEfficiency += 0.5;
                    this.updateAIMetrics();
                } else {
                    clearInterval(restoreInterval);
                    this.showNotification('AI models retrained - efficiency restored', 'success');
                }
            }, 5000);
        }
    }

    refreshLiveStats() {
        // Simulate API call delay
        const refreshBtn = document.querySelector('.refresh-btn');
        if (refreshBtn) {
            refreshBtn.disabled = true;
            refreshBtn.innerHTML = `
        <i class="icon-refresh spinning"></i> Refreshing...
      `;
        }

        setTimeout(() => {
            // Update stats with new random values
            this.demoData.vehicleCount += Math.floor(Math.random() * 100) - 50;
            this.demoData.averageSpeed += (Math.random() - 0.5) * 2;
            this.demoData.congestionIndex += (Math.random() - 0.5) * 5;
            this.demoData.signalEfficiency += (Math.random() - 0.5) * 2;

            // Keep values in realistic ranges
            this.demoData.averageSpeed = Math.max(15, Math.min(45, this.demoData.averageSpeed));
            this.demoData.congestionIndex = Math.max(0, Math.min(100, this.demoData.congestionIndex));
            this.demoData.signalEfficiency = Math.max(80, Math.min(100, this.demoData.signalEfficiency));

            this.updateLiveStats();

            if (refreshBtn) {
                refreshBtn.disabled = false;
                refreshBtn.innerHTML = `
          <i class="icon-refresh"></i> Refresh
        `;
            }

            this.showNotification('Stats updated successfully', 'success');
        }, 1500);
    }

    updateLiveStats() {
        // Update vehicle count
        const vehicleElement = document.getElementById('vehicle-count');
        if (vehicleElement) {
            vehicleElement.textContent = this.demoData.vehicleCount.toLocaleString();
        }

        // Update other stats
        const statsMap = {
            'average-speed': `${this.demoData.averageSpeed.toFixed(1)} mph`,
            'congestion-index': `${this.demoData.congestionIndex.toFixed(1)}%`,
            'signal-efficiency': `${this.demoData.signalEfficiency.toFixed(1)}%`
        };

        Object.entries(statsMap).forEach(([id, value]) => {
            const element = document.getElementById(id);
            if (element) element.textContent = value;
        });
    }

    updateOverviewMetrics() {
        const metrics = {
            'system-health': `${this.demoData.systemHealth.toFixed(1)}%`,
            'active-intersections': `${this.demoData.activeIntersections}/${this.demoData.totalIntersections}`,
            'ai-efficiency': `${this.demoData.aiEfficiency.toFixed(1)}%`,
            'current-flow': `${this.demoData.currentFlow.toLocaleString()} veh/hr`
        };

        Object.entries(metrics).forEach(([id, value]) => {
            const element = document.getElementById(id);
            if (element) element.textContent = value;
        });
    }

    updateIntersectionMap() {
        // Randomly update some intersection statuses
        const points = document.querySelectorAll('.intersection-point');
        points.forEach((point, index) => {
            if (Math.random() < 0.1) { // 10% chance to change
                const statuses = ['optimal', 'good', 'warning', 'critical'];
                const weights = [0.7, 0.2, 0.08, 0.02];
                const newStatus = this.weightedRandom(statuses, weights);

                // Remove old status classes
                point.classList.remove('optimal', 'good', 'warning', 'critical');
                point.classList.add(newStatus);
                point.title = `Intersection ${index + 1}: ${newStatus.toUpperCase()}`;
            }
        });
    }

    updateAIMetrics() {
        // Update AI efficiency gauge
        const gauge = document.querySelector('.gauge');
        if (gauge) {
            gauge.style.setProperty('--percentage', this.demoData.aiEfficiency);
        }

        const gaugeValue = document.querySelector('.gauge-value');
        if (gaugeValue) {
            gaugeValue.textContent = `${this.demoData.aiEfficiency.toFixed(1)}%`;
        }

        // Update performance metrics
        const performanceMetrics = {
            accuracy: 95.7 + (Math.random() - 0.5) * 1,
            precision: 94.2 + (Math.random() - 0.5) * 1,
            recall: 96.1 + (Math.random() - 0.5) * 1,
            f1Score: 95.1 + (Math.random() - 0.5) * 1
        };

        Object.entries(performanceMetrics).forEach(([metric, value]) => {
            const progressBar = document.querySelector(`[data-metric="${metric}"] .progress`);
            if (progressBar) {
                progressBar.style.width = `${value.toFixed(1)}%`;
            }

            const valueElement = document.querySelector(`[data-metric="${metric}"] .metric-value`);
            if (valueElement) {
                valueElement.textContent = `${value.toFixed(1)}%`;
            }
        });
    }

    updateEmergencyStatus() {
        // Update emergency metrics
        const responseMetrics = {
            'response-time': '-35%',
            'success-rate': '99.7%',
            'activation-time': `${(1.5 + Math.random()).toFixed(1)}s`,
            'routes-created': Math.floor(47 + Math.random() * 10)
        };

        Object.entries(responseMetrics).forEach(([id, value]) => {
            const element = document.getElementById(id);
            if (element) element.textContent = value;
        });
    }

    simulateDataUpdates() {
        // Simulate real-time data changes
        this.demoData.systemHealth += (Math.random() - 0.5) * 0.2;
        this.demoData.aiEfficiency += (Math.random() - 0.5) * 0.3;
        this.demoData.currentFlow += Math.floor((Math.random() - 0.5) * 200);
        this.demoData.vehicleCount += Math.floor(Math.random() * 50) - 25;

        // Keep values in realistic ranges
        this.demoData.systemHealth = Math.max(95, Math.min(100, this.demoData.systemHealth));
        this.demoData.aiEfficiency = Math.max(90, Math.min(100, this.demoData.aiEfficiency));
        this.demoData.currentFlow = Math.max(1500, Math.min(4000, this.demoData.currentFlow));

        // Randomly change intersection availability
        if (Math.random() < 0.05) { // 5% chance
            const change = Math.random() < 0.5 ? -1 : 1;
            this.demoData.activeIntersections = Math.max(240, Math.min(250, this.demoData.activeIntersections + change));
        }

        // Update displays based on current tab
        this.initializeTabFeatures(this.currentTab);

        // Update chart
        this.initializeCharts();
    }

    startDataSimulation() {
        // Simulate continuous data updates
        setInterval(() => {
            this.updateTimestamps();
        }, 1000);

        setInterval(() => {
            this.simulateDataUpdates();
        }, 30000);
    }

    updateTimestamps() {
        this.currentTime = new Date();
        const timeString = this.formatTime(this.currentTime);

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

    formatTime(date) {
        return date.toISOString().slice(0, 19).replace('T', ' ') + ' UTC';
    }

    calculateSessionDuration() {
        const now = this.currentTime;
        const start = this.sessionStart;
        const diff = now - start;

        const hours = Math.floor(diff / (1000 * 60 * 60));
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

        return `${hours}h ${minutes}m`;
    }

    showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;

        const colors = {
            success: '#4CAF50',
            error: '#F44336',
            warning: '#FF9800',
            info: '#2196F3'
        };

        notification.style.cssText = `
      position: fixed;
      top: 80px;
      right: 20px;
      background: ${colors[type] || colors.info};
      color: white;
      padding: 12px 24px;
      border-radius: 6px;
      z-index: 10000;
      transform: translateX(100%);
      transition: transform 0.3s ease;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
      font-weight: 500;
      max-width: 300px;
    `;

        document.body.appendChild(notification);

        // Animate in
        setTimeout(() => {
            notification.style.transform = 'translateX(0)';
        }, 100);

        // Remove after 4 seconds
        setTimeout(() => {
            notification.style.transform = 'translateX(100%)';
            setTimeout(() => {
                if (document.body.contains(notification)) {
                    document.body.removeChild(notification);
                }
            }, 300);
        }, 4000);
    }

    // Intersection control methods
    optimizeIntersection(id) {
        this.showNotification(`Optimizing intersection ${id + 1}...`, 'info');

        // Simulate optimization
        setTimeout(() => {
            this.showNotification(`Intersection ${id + 1} optimized (+8% efficiency)`, 'success');
        }, 2000);
    }

    manualControl(id) {
        if (this.userLevel < 2) {
            this.showNotification('Insufficient privileges for manual control', 'error');
            return;
        }

        this.showNotification(`Manual control activated for intersection ${id + 1}`, 'warning');

        // Auto-release after 5 minutes
        setTimeout(() => {
            this.showNotification(`Manual control released for intersection ${id + 1}`, 'info');
        }, 300000);
    }

    modifyRoute(type) {
        this.showNotification(`Route modification panel opened for ${type}`, 'info');
    }

    cancelRoute(type) {
        this.showNotification(`Emergency route ${type} cancelled`, 'warning');

        // Remove route from list
        const routeElements = document.querySelectorAll('.emergency-route-item');
        routeElements.forEach(element => {
            if (element.textContent.includes(type)) {
                element.remove();
            }
        });

        // Update count
        const routeList = document.getElementById('emergency-route-list');
        const routeCount = document.querySelector('.route-count');
        const noRoutes = document.querySelector('.no-routes');

        if (routeList && routeList.children.length === 0) {
            routeList.style.display = 'none';
            if (noRoutes) noRoutes.style.display = 'block';
        }

        if (routeCount) {
            routeCount.textContent = `${routeList ? routeList.children.length : 0} Active`;
        }
    }
}

// Initialize demo when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.trafficDemo = new TrafficDemo();
});

// CSS for demo-specific styles
const demoStyles = `
  .spinning {
    animation: spin 1s linear infinite;
  }
  
  @keyframes spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }
  
  .modal-content {
    background: var(--bg-card);
    border: 1px solid var(--border-primary);
    border-radius: 12px;
    max-width: 500px;
    width: 90%;
    max-height: 80vh;
    overflow-y: auto;
  }
  
  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: var(--space-lg);
    border-bottom: 1px solid var(--border-secondary);
  }
  
  .modal-header h3 {
    color: var(--text-primary);
    margin: 0;
  }
  
  .modal-close {
    background: none;
    border: none;
    color: var(--text-secondary);
    font-size: 1.5rem;
    cursor: pointer;
    padding: var(--space-sm);
    border-radius: 4px;
  }
  
  .modal-close:hover {
    background-color: var(--bg-surface);
    color: var(--text-primary);
  }
  
  .modal-body {
    padding: var(--space-lg);
  }
  
  .intersection-details {
    display: flex;
    flex-direction: column;
    gap: var(--space-md);
  }
  
  .detail-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: var(--space-md) 0;
    border-bottom: 1px solid var(--border-secondary);
  }
  
  .detail-row:last-of-type {
    border-bottom: none;
  }
  
  .detail-row .label {
    color: var(--text-secondary);
    font-weight: 500;
  }
  
  .detail-row .value {
    color: var(--text-primary);
    font-family: var(--font-mono);
    font-weight: 600;
  }
  
  .status-optimal { color: var(--secondary-green); }
  .status-good { color: var(--primary-blue); }
  .status-warning { color: var(--warning-orange); }
  .status-critical { color: var(--error-red); }
  
  .detail-actions {
    display: flex;
    gap: var(--space-md);
    margin-top: var(--space-lg);
    padding-top: var(--space-lg);
    border-top: 1px solid var(--border-secondary);
  }
  
  .btn-sm {
    padding: var(--space-sm) var(--space-md);
    font-size: 0.875rem;
  }
  
  .btn-error {
    background-color: var(--error-red);
    color: white;
  }
  
  .btn-error:hover {
    background-color: #d32f2f;
  }
  
  .emergency-route-item {
    background: var(--gradient-card);
    border: 1px solid var(--border-primary);
    border-radius: 8px;
    padding: var(--space-lg);
    margin-bottom: var(--space-md);
  }
  
  .route-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: var(--space-md);
  }
  
  .route-header h4 {
    color: var(--text-primary);
    margin: 0;
  }
  
  .route-priority {
    padding: var(--space-xs) var(--space-sm);
    border-radius: 12px;
    font-size: 0.75rem;
    font-weight: 600;
    text-transform: uppercase;
  }
  
  .route-priority.critical {
    background-color: rgba(244, 67, 54, 0.2);
    color: var(--error-red);
  }
  
  .route-priority.high {
    background-color: rgba(255, 152, 0, 0.2);
    color: var(--warning-orange);
  }
  
  .route-priority.medium {
    background-color: rgba(33, 150, 243, 0.2);
    color: var(--primary-blue);
  }
  
  .route-details {
    display: flex;
    flex-direction: column;
    gap: var(--space-xs);
    margin-bottom: var(--space-md);
    color: var(--text-secondary);
    font-size: 0.875rem;
  }
  
  .route-actions {
    display: flex;
    gap: var(--space-md);
  }
  
  .optimization-item.completed {
    opacity: 0.7;
  }
  
  .progress-circle {
    position: relative;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: conic-gradient(
      var(--primary-blue) calc(var(--progress) * 1%),
      var(--bg-surface) calc(var(--progress) * 1%)
    );
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .progress-circle::before {
    content: '';
    position: absolute;
    width: 28px;
    height: 28px;
    background-color: var(--bg-card);
    border-radius: 50%;
  }
  
  .progress-circle span {
    position: relative;
    z-index: 1;
    font-size: 0.75rem;
    font-weight: 600;
    color: var(--text-primary);
  }
`;

// Inject demo styles
const styleSheet = document.createElement('style');
styleSheet.textContent = demoStyles;
document.head.appendChild(styleSheet);

