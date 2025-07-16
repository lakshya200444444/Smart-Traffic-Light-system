import joblib
import numpy as np

# Load model and scaler
model = joblib.load("traffic_predictor.pkl")
scaler = joblib.load("traffic_scaler.pkl")

# Example input (replace with your own)
input_data = {
    "hour": 17,
    "dayofweek": 1,                 # Tuesday
    "is_weekend": 0,                # Not weekend
    "temperature": 31.5,            # Celsius
    "humidity": 72.0,               # Percent
    "rain": 0.1,                    # mm
    "wind_speed": 8.0,              # km/h
    "vehicle_count_last_5min": 50,
    "weather_condition": 0          # Assume 0 = Clear
}

# Order of features must match training
features = [
    input_data["hour"],
    input_data["dayofweek"],
    input_data["is_weekend"],
    input_data["temperature"],
    input_data["humidity"],
    input_data["rain"],
    input_data["wind_speed"],
    input_data["vehicle_count_last_5min"],
    input_data["weather_condition"]
]

# Reshape and scale input
X = np.array(features).reshape(1, -1)
X_scaled = scaler.transform(X)

# Predict
predicted_vehicle_count = model.predict(X_scaled)[0]
print(f"🚗 Predicted Vehicle Count: {predicted_vehicle_count:.2f}")