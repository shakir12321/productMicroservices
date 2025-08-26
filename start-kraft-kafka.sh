#!/bin/bash

echo "🚀 Starting Kafka with KRaft (No Zookeeper)..."

# Stop any existing containers
echo "🛑 Stopping existing containers..."
docker compose -f docker-compose-kraft.yml down

# Start Kafka with KRaft
echo "📡 Starting Kafka with KRaft..."
docker compose -f docker-compose-kraft.yml up -d kafka

# Wait for Kafka to be ready
echo "⏳ Waiting for Kafka to be ready..."
sleep 30

# Check Kafka status
echo "🔍 Checking Kafka status..."
docker compose -f docker-compose-kraft.yml logs kafka --tail=10

# Start Kafka UI
echo "🖥️ Starting Kafka UI..."
docker compose -f docker-compose-kraft.yml up -d kafka-ui

# Wait for UI to start
sleep 10

# Show status
echo "📊 Service Status:"
docker compose -f docker-compose-kraft.yml ps

echo ""
echo "🎉 Kafka KRaft is ready!"
echo ""
echo "📱 Access Points:"
echo "   Kafka Broker: localhost:9092"
echo "   Kafka UI: http://localhost:8080"
echo ""
echo "🔍 Monitor Kafka:"
echo "   docker compose -f docker-compose-kraft.yml logs kafka -f"
echo ""
echo "📋 KRaft Benefits:"
echo "   ✅ No Zookeeper dependency"
echo "   ✅ Better performance"
echo "   ✅ Simplified architecture"
echo "   ✅ Future-proof setup"
