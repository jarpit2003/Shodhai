import "./globals.css";
import type { ReactNode } from "react";

export const metadata = { title: "Contest UI", description: "Live coding contest" };

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
