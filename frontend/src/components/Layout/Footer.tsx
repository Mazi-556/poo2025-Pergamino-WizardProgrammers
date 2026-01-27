const Footer = () => {
  return (
    <footer className="bg-white border-t py-6 mt-auto">
      <div className="container mx-auto px-4 text-center text-gray-600">
        <p>&copy; {new Date().getFullYear()} Wizard Programmers - UNNOBA</p>
        <p className="text-sm mt-1">Trabajo Practico de POO</p>
      </div>
    </footer>
  );
};

export default Footer;