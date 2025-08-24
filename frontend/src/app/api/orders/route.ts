import { NextResponse } from "next/server";

// Dummy data for orders
const dummyOrders = [
  {
    id: 1,
    customerName: "John Doe",
    customerEmail: "john.doe@example.com",
    items: [{ productId: 1, quantity: 1, price: 999.99 }],
    totalAmount: 999.99,
    status: "DELIVERED",
    createdAt: "2024-08-24T09:00:00Z",
  },
  {
    id: 2,
    customerName: "Jane Smith",
    customerEmail: "jane.smith@example.com",
    items: [
      { productId: 2, quantity: 1, price: 1199.99 },
      { productId: 3, quantity: 2, price: 129.99 },
    ],
    totalAmount: 1459.97,
    status: "SHIPPED",
    createdAt: "2024-08-24T10:30:00Z",
  },
  {
    id: 3,
    customerName: "Mike Johnson",
    customerEmail: "mike.johnson@example.com",
    items: [{ productId: 4, quantity: 1, price: 699.99 }],
    totalAmount: 699.99,
    status: "CONFIRMED",
    createdAt: "2024-08-24T11:15:00Z",
  },
  {
    id: 4,
    customerName: "Sarah Wilson",
    customerEmail: "sarah.wilson@example.com",
    items: [{ productId: 5, quantity: 1, price: 89.99 }],
    totalAmount: 89.99,
    status: "PENDING",
    createdAt: "2024-08-24T12:00:00Z",
  },
  {
    id: 5,
    customerName: "David Brown",
    customerEmail: "david.brown@example.com",
    items: [
      { productId: 1, quantity: 1, price: 999.99 },
      { productId: 5, quantity: 1, price: 89.99 },
    ],
    totalAmount: 1089.98,
    status: "CANCELLED",
    createdAt: "2024-08-24T13:45:00Z",
  },
];

export async function GET() {
  try {
    // Try to connect to the actual microservice first
    const response = await fetch("http://order-service:8082/api/orders", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json({ data });
    }
  } catch (error) {
    console.log("Microservice not available, using dummy data");
  }

  // Return dummy data if microservice is not available
  return NextResponse.json({ data: dummyOrders });
}

export async function POST(request: Request) {
  try {
    const body = await request.json();

    // Try to connect to the actual microservice first
    const response = await fetch("http://order-service:8082/api/orders", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json(data);
    }
  } catch (error) {
    console.log("Microservice not available, using dummy response");
  }

  // Return dummy response if microservice is not available
  const newOrder = {
    id: Math.floor(Math.random() * 1000) + 6,
    ...body,
    createdAt: new Date().toISOString(),
  };

  return NextResponse.json(newOrder);
}
