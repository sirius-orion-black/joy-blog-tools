import { webConfigStore } from "@/store/webConfigStore";

const About = () => {
  const { webConfig } = webConfigStore();

  return (
    <div className="page">
      <div className="page-top about-top">
        <div className="top-info">
          <span>
            <p>{webConfig.webAuthor}</p>
            <p>{webConfig.authorSignature}</p>
          </span>
          <img className="avatar" src={webConfig.headPortrait} />
        </div>
      </div>
    </div>
  );
};

export default About;
