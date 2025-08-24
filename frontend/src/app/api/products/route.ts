import { NextResponse } from "next/server";

// Dummy data for products
const dummyProducts = [
  {
    id: 1,
    name: "iPhone 15 Pro",
    description: "Latest iPhone with advanced camera system",
    price: 999.99,
    stockQuantity: 50,
    category: "Electronics",
  },
  {
    id: 2,
    name: "MacBook Air M2",
    description: "Lightweight laptop with powerful M2 chip",
    price: 1199.99,
    stockQuantity: 25,
    category: "Electronics",
  },
  {
    id: 3,
    name: "Nike Air Max 270",
    description: "Comfortable running shoes with air cushioning",
    price: 129.99,
    stockQuantity: 100,
    category: "Sports",
  },
  {
    id: 4,
    name: "Samsung 4K TV",
    description: "55-inch 4K Ultra HD Smart TV",
    price: 699.99,
    stockQuantity: 15,
    category: "Electronics",
  },
  {
    id: 5,
    name: "Coffee Maker",
    description: "Programmable coffee maker with thermal carafe",
    price: 89.99,
    stockQuantity: 75,
    category: "Home & Kitchen",
  },
];

export async function GET() {
  try {
    // Try to connect to the actual microservice first
    const response = await fetch("http://product-service:8081/api/products", {
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
  return NextResponse.json({ data: dummyProducts });
}

export async function POST(request: Request) {
  try {
    const body = await request.json();

    // Try to connect to the actual microservice first
    const response = await fetch("http://product-service:8081/api/products", {
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
  const newProduct = {
    id: Math.floor(Math.random() * 1000) + 6,
    ...body,
  };

  return NextResponse.json(newProduct);
}
